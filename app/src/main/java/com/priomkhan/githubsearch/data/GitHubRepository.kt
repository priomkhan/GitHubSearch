package com.priomkhan.githubsearch.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.priomkhan.githubsearch.GITHUB_SERVICE_URL
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.ui.shared.SharedViewModel
import com.priomkhan.githubsearch.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class GitHubRepository (val app: Application){
    val userSearchLocalData = MutableLiveData<List<UserSearchResult>>()
    val userOnlineData = MutableLiveData<List<GitHubUser>>()
    private val userDao = UserDatabase.getDatabase(app).userDao()
    private lateinit var httpClient: OkHttpClient
    private lateinit var viewModel: SharedViewModel
    //Authentication of user
    val localUserInfo = MutableLiveData<LocalUser>()
    val localUserInfoExist = MutableLiveData<Boolean>()
    val userSearchQuery = MutableLiveData<String>()

    val busyBool = MutableLiveData<Boolean>()

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

        checkIfUserInfoExist()

   }


    fun userLogin(userName: String, password: String){
        var bool = false
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
            val localUser = LocalUser(0,userName,password)
            userDao.insertUser(localUser)
            bool= checkIfUserInfoExist()
            if(bool){
                Log.i(LOG_TAG, "User Now Exist in Local Database")
            }
        }
    }

    fun logout(){
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
            userSearchLocalData.postValue(emptyList())
            userOnlineData.postValue(emptyList())
            userSearchQuery.postValue("")
            localUserInfoExist.postValue(false)
            localUserInfo.postValue(null)
        }
    }

    fun checkIfUserInfoExist(): Boolean{
        var isExist = false
        CoroutineScope(Dispatchers.IO).launch {
            val data = userDao.getAll()
            if(data.isEmpty()){
                localUserInfoExist.postValue(false)
                Log.i(LOG_TAG,"Local Database is Empty" )
                isExist = false
            }else{
                localUserInfo.postValue(data[0])
                localUserInfoExist.postValue(true)
                Log.i(LOG_TAG,"Local Data: ${data.get(0).toString()}" )
                isExist = true
                refreshData()

            }
        }

        return isExist
    }

    fun refreshData() {
        val query= userSearchQuery.value?:""
        if(query.isNotBlank()&&query.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch{
                Log.i(LOG_TAG,"GitHubRepository: Auto Query Search: ${query}")
                getData(query)
            }
        }

    }

    fun refreshData(query: String) {

        if(query.isNotBlank()&&query.isNotEmpty()){
            userSearchQuery.postValue(query)
            val job = CoroutineScope(Dispatchers.IO).launch{
                Log.i(LOG_TAG,"GitHubRepository: Manual Query Search: ${query}")
                getData(query)
            }
        }else{
            userSearchQuery.postValue("")
        }

    }

    fun getData(query: String) = runBlocking { // this: CoroutineScope
        launch {
            busyBool.postValue(true)
            Log.i(LOG_TAG,"Task from runBlocking to get search result")

            //delay(200L)
        }

        coroutineScope { // Creates a coroutine scope to get each user details
            launch {

                Log.i(LOG_TAG,"Task from nested launch to get each user details")
                callGitHubWebService(query)
                //delay(200L)
            }

        }
        //delay(200L)
        Log.i(LOG_TAG,"getData() Coroutine scope is over") // This line is not printed until the nested launch completes
        busyBool.postValue(false)
    }



    @WorkerThread
    suspend fun callGitHubWebService(query: String) {
        val username = localUserInfo.value?.userName
        val password = localUserInfo.value?.password
        if(networkAvailable()){


            Log.i(LOG_TAG, "Network Available: Getting Data....")
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8)
            val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val builder = original.newBuilder()
                        .header("Accept", "application/vnd.github.v3+json")
                        .header("Authorization", authToken)
                    val request = builder.build()
                    chain.proceed(request)
                }
                .build()



            val retrofit = Retrofit.Builder()
                .baseUrl(GITHUB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi)) //we used moshi builder to map the json to class property
                .client(httpClient)
                .build()

            val service = retrofit.create(GitHubUserSearchService::class.java)

            val serviceData = service.getUserSearchData(query).body()?: UserSearchList(0, emptyList())

            if(serviceData.total_count>0){
                val total_result = serviceData.total_count
                val userSearchList = serviceData.items
                Log.i(LOG_TAG, "Total Result Found: ${total_result} \n")
                val job = CoroutineScope(Dispatchers.IO).launch {
                    getDetails(userSearchList)
                }

                val queue = job.join()
                Log.i(LOG_TAG, "Getting Details Coroutine Done: ${queue} \n")
                //userSearchOnlineData.postValue(serviceData.items)

            }

        }

    }


    @WorkerThread
    suspend fun getDetails(userSearchList: List<UserSearchResult>){
        val userList = ArrayList<GitHubUser>()
        //Log.i(LOG_TAG, "#getDetails: userList Size: ${userSearchList.size}")
        //delay(5000)
        for(user in userSearchList){


            val job = CoroutineScope(Dispatchers.IO).launch {

                //Log.i(LOG_TAG, "#getDetails: user: {${user.userName}}: ${userDetails.name}")

                userList.add(
                    GitHubUser(user,
                    getUserDetails(user),
                        getRepos(user)))

            }

            val queue = job.join()
            //Log.i(LOG_TAG,"Total User Details Received($queue): userList size: ${userList.size}")
            userOnlineData.postValue(userList)
        }
    }




    @WorkerThread
    suspend fun getUserDetails(user: UserSearchResult): UserDetails{
        val userName = user.userName
        val username = localUserInfo.value?.userName
        val password = localUserInfo.value?.password
        //Log.i(LOG_TAG, "GitHubRepository: getting data for (${userName})")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", authToken)
                val request = builder.build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_SERVICE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //we used moshi builder to map the json to class property
            .client(httpClient)
            .build()
        val service = retrofit.create(GitHubUserDetailsService::class.java)

        val serviceData = service.getUserDetailsData(userName).body()
        if (serviceData != null) {
                //Log.i(LOG_TAG, "${user.userName} Details: ${serviceData.toString()}")
            return serviceData
        }else{
            return UserDetails("n/a","n/a","n/a","n/a","n/a",0,0,0)
        }
    }

    @WorkerThread
    suspend fun getRepos(user: UserSearchResult): List<UserRepo> {
        val userName = user.userName
        val username = localUserInfo.value?.userName
        val password = localUserInfo.value?.password

        Log.i(LOG_TAG, "GitHubRepository: getting repo data for (${userName})")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8)
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("Authorization", authToken)
                val request = builder.build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(GITHUB_SERVICE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //we used moshi builder to map the json to class property
            .client(httpClient)
            .build()
        val service = retrofit.create(GitHubUserRepoService::class.java)
        val response = service.getUserRepos(userName).body()?: emptyList()

        if(response.size >0 ){
            Log.i(LOG_TAG, "Repo Size: ${response.size}")
        }else{
            Log.i(LOG_TAG, "No public repository found ")
        }

        return response
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