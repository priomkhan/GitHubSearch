package com.priomkhan.githubsearch.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.priomkhan.githubsearch.data.GitHubRepository


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

    //note: passing the app ref
    private val dataRepo = GitHubRepository(app)

    //val userData = dataRepo.userSearchLocalData

    val userData = dataRepo.userSearchOnlineData





}
