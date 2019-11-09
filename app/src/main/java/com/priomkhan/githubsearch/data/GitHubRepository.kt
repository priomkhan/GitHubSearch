package com.priomkhan.githubsearch.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class GitHubRepository (val app: Application){
    val userSearchData = MutableLiveData<List<UserSearchResult>>()
    /*
    'listType' a parameter rise Type.
    We will using this more than once, so we declare this value as a property of the viewModel class.
    And this creates a custom type that I can use to create or parse JSON content.
     */
    private val listType = Types.newParameterizedType(List::class.java, UserSearchResult::class.java)
    /*
    Here we create an init block, and we will simply call getUserSearchData,
    so that we can retrieve the data as the repository is created.
    */

    init {
        //Check Internet Connectivity
        Log.i(LOG_TAG, "Network Available: ${networkAvailable()}")
        getUserLocalData()

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
        userSearchData.value = adapter.fromJson(textReadFromAssets)

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