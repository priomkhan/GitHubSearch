package com.priomkhan.githubsearch.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.utilities.FileHelper

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

    }
}
