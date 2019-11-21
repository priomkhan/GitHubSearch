package com.priomkhan.githubsearch.ui.shared

import android.app.Application
import android.net.wifi.hotspot2.pps.Credential
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.data.*
import com.priomkhan.githubsearch.ui.details.DetailedRepoRecyclerAdapter


/*
Step:4
//Now we gonna call FileHelper.getTextFromAssets() function from my ViewModel class.
// The getData view model is associated with the getData fragment, and
// it's where we should do all the work of acquiring our user data .

//In order to call that function we need a context
// and right now we don't have one. The easiest way to get the application
// context from within the view model is to extend a class called AndroidViewModel. (Change ViewModel to AndroidViewModel())

//Now we change our class's primary constructor
// so that it receives a reference to the application,
// We will name it app, and then passes that to the AndroidViewModel's constructor.
 */

class SharedViewModel(app: Application) : AndroidViewModel(app) {

    //note: passing the app ref
    private val dataRepo = GitHubRepository(app)
    val userOnlineData = dataRepo.userOnlineData
    val userSearchQuery = dataRepo.userSearchQuery
    val selectedUser = MutableLiveData<GitHubUser>()

    val ifLocalUserExist = dataRepo.localUserInfoExist

    lateinit var  adapter : DetailedRepoRecyclerAdapter

    fun refreshData() {
        dataRepo.refreshData()
    }

    fun checkIfUserExist(): Boolean{
        return dataRepo.checkIfUserInfoExist()
    }

    fun search(query : String){
        Log.i(LOG_TAG," SharedViewModel: New Query Search: ${query}")
        dataRepo.refreshData(query)
    }

    fun login(userName: String, password:String){
       dataRepo.userLogin(userName,password)
    }

    fun clearUserData(){
        dataRepo.userOnlineData.postValue(emptyList())
        dataRepo.refreshData()
    }

    fun logout(){
        dataRepo.logout()
    }
}
