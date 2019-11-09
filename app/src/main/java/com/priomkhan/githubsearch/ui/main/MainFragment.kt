package com.priomkhan.githubsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.UserSearchResult
import kotlinx.android.synthetic.main.main_fragment.*
import java.lang.StringBuilder

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.monsterData.observe(this, Observer {
            val userNames = StringBuilder()

            for (user in it){
                userNames.append(user.userName).append("\n")
            }
            //Displaying data on Screen
            message.text = userNames
        })
        /*
        Step:2
        Testing Initial Output
         */
        val newUser = UserSearchResult("priomkhan","myUrl","myAvatarUrl","myReposUrl")
        Log.i("GitHubSearch", newUser.toString())
        //End Testing

        return inflater.inflate(R.layout.main_fragment, container, false)
    }


}
