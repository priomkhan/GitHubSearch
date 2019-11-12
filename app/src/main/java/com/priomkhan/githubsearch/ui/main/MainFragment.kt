package com.priomkhan.githubsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.UserDetails
import com.priomkhan.githubsearch.data.UserSearchResult
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private var count =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


//        viewModel.userSearchOnlineData.observe(this, Observer {
//            val userNames = StringBuilder()
//
//
//                count = it.size
//
//                for (user in it){
//
//                    userNames.append(user.userName).append("\n")
//
//                }
//
//                //Displaying data on Screen
//                message.text = userNames
//                //viewModel.getUserDetails()
//
//
//        })

//        viewModel.userDetailsOnlineData.observe(this, Observer {
//            val userDetails = StringBuilder()
//            Log.i(LOG_TAG, "userDetailsData Changed: Size: ${it.size}")
//            for (user in it){
//                //Log.i(LOG_TAG, "${user.toString()} \n")
//                userDetails.append(user.name).append(": ").append(user.email).append("\n")
//
//            }
//
//
//        })

        viewModel.userOnlineData.observe(this, Observer {

            val adapter = MainRecyclerAdapter(requireContext(),it)
            recyclerView.adapter = adapter


//            val userNames = StringBuilder()
//             count = it.size
//
//                for (user in it){
//
//                    userNames.append(user.userSearchResult?.userName)
//                        .append(" : ")
//                        .append(user.userDetails?.name)
//                        .append(" followers : ")
//                        .append(user.userDetails?.followers)
//                        .append(" following : ")
//                        .append(user.userDetails?.following)
//                        .append("\n")
//                }

                //Displaying data on Screen
                //message.text = userNames
                //viewModel.getUserDetails()


        })

        /*
        Step:2
        Testing Initial Output
         */
        //val newUser = UserSearchResult("priomkhan", "myUrl", "myAvatarUrl", "myReposUrl")
        //Log.i("GitHubSearch", newUser.toString())
        //End Testing

        return view
    }


}
