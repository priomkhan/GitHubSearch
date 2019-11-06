package com.priomkhan.githubsearch.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.data.User_SearchResult
import com.priomkhan.githubsearch.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/*
Step:4
//Now we gonna call FileHelper.getTextFromAssets() function from my ViewModel class.
// The main view model is associated with the main fragment, and
// it's where we should do all the work of acquiring our user data .

//In order to call that function we need a context
// and right now we don't have one. The easiest way to get the application
// context from within the view model is to extend a class called AndroidViewModel. (Change ViewModel to AndroidViewModel())

//Now we change our class's primary constructor
// so that it receives a reference to the application,
// We will name it app, and then passes that to the AndroidViewModel's constructor.
 */

class MainViewModel(app: Application) : AndroidViewModel(app) {

    /*
   'listType' a parameter rise Type.
   We will using this more than once, so we declare this value as a property of the viewModel class.
    And this creates a custom type that I can use to create or parse JSON content.
    */

    private val listType = Types.newParameterizedType(List::class.java,User_SearchResult::class.java)

    //I'll create an init block that will be executed when the view model is instantiated.
    // Then I'll create a variable named text.
    // I'll get it from filehelper.getTextFromResources,
    init{
        // Type the letter R and scroll to the top of this list
        // and choose the version of the class from your apps base package.
        // Mine is com.priomkhan.readjsonlocal. Then from there, call raw and your file name (monster_data).

        //Read Data from Assets:
        val textReadFromAssets = FileHelper.getTextFromAssets(app, "user_data.json")
        Log.i(LOG_TAG, textReadFromAssets)

        parsetext(textReadFromAssets)

    }



    /*
    //Function to Parse JSON strings with Moshi
    // I'll add a new function that I'll call parseText, and it will receive a text value typed as a string.
 */

    fun parsetext(text: String){
        /*
         //'moshi' is a variable that  represents an instance of the Moshi library.
        // And we use a builder pattern to create the instance with Moshi.Builder.
         */
        // If no annotation used
        val moshi = Moshi.Builder().build()




        /*
        //Creating an adapter object
         */
        val adapter : JsonAdapter<List<User_SearchResult>> = moshi.adapter(listType)

        /*
        //Parsing the file
         */
        val userListData = adapter.fromJson(text)

        /*
        //Here we receiving a list of objects, so we can use for loop to access each object
         */

        for(user in  userListData?: emptyList()){

            Log.i(LOG_TAG,"${user.login} {\$${user.avatar_url}} {\$${user.repos_url}")
        }

    }
}
