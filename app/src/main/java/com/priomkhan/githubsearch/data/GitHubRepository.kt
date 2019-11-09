package com.priomkhan.githubsearch.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.priomkhan.githubsearch.GITHUB_SERVICE_URL
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GitHubRepository (val app: Application){
    val userSearchLocalData = MutableLiveData<List<UserSearchResult>>()
    val userSearchOnlineData = MutableLiveData<List<UserSearchResult>>()

    /*
    'listType' a parameter rise Type.
    We will using this more than once, so we declare this value as a property of the viewModel class.
    And this creates a custom type that I can use to create or parse JSON content.
     */
    private val listType = Types.newParameterizedType(List::class.java, UserSearchResult::class.java)
    /*
    Here we create an init block, and we will simply call getUserSearchLocalData,
    so that we can retrieve the data as the repository is created.
    */

    init {
        //Check Internet Connectivity
        Log.i(LOG_TAG, "Network Available: ${networkAvailable()}")
        getUserLocalData()

        CoroutineScope(Dispatchers.IO).launch {
            callGitHubWebService()
        }

    }

    @WorkerThread
    suspend fun callGitHubWebService() {
        if(networkAvailable()){
            Log.i(LOG_TAG, "Network Available: Getting Data....")
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit = Retrofit.Builder()
                .baseUrl(GITHUB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi)) //we used moshi builder to map the json to class property
                .build()

            val service = retrofit.create(GitHubUserSearchService::class.java)

            val serviceData = service.getUserSearchData().body()?: UserSearchList(0, emptyList())

            if(serviceData.total_count>0){


                    userSearchOnlineData.postValue(serviceData.items)


            }



//            var retriever = service.getUserSearchData()
//
//            val callback = object : Callback<List<UserSearchResult>> {
//
//                override fun onFailure(call: Call<List<UserSearchResult>>, t: Throwable) {
//                    Log.e("MainActivity", "Problem calling API", t)
//                }
//
//                override fun onResponse(call: Call<List<UserSearchResult>>, response: Response<List<UserSearchResult>>) {
//                    response?.isSuccessful.let {
//                        Log.e("MainActivity_Call", "Successful")
//                        userSearchOnlineData.postValue(response?.body())
//
//                        Log.e(LOG_TAG, userSearchOnlineData.s)
//                        //  mainAdapter = MainAdapter(this@MainActivity.users!!, this@MainActivity)
//
//                        //  recyclerView.adapter = mainAdapter
//                    }
//                }
//
//
//
//            }
//
//            retriever.(callback)

        }



    }

    /*
    //Function to Parse JSON strings with Moshi
    // I'll add a new function that I'll call parseText, and it will receive a text value typed as a string.
  */
    private fun getUserLocalData() {
        //Read Data from Assets:
        val textReadFromAssets = FileHelper.getTextFromAssets(app, "user_data.json")

        /*
        //'moshi' is a variable that  represents an instance of the Moshi library.
        // And we use a builder pattern to create the instance with Moshi.Builder.
        */

        // If no annotation used
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        /*
        //Creating an adapter object
         */
        val adapter : JsonAdapter<List<UserSearchResult>> = moshi.adapter(listType)

        /*
        //Parsing the file and return as a list
         */
        userSearchLocalData.value = adapter.fromJson(textReadFromAssets)


    }

    //This function check if the mobile is connected to the Wifi or Mobile internet, however
    // it does not work above Android O.
    @Suppress("DEPRECATION")
    private fun networkAvailable():Boolean{
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}