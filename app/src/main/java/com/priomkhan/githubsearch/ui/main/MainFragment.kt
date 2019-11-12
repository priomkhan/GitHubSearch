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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.GitHubUser
import com.priomkhan.githubsearch.data.UserDetails
import com.priomkhan.githubsearch.data.UserSearchResult
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class MainFragment : Fragment(),
    MainRecyclerAdapter.UserItemListener {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout // References to the swiperefreshlayout of main_fragment.xml

    private var count =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.userOnlineData.observe(this, Observer {

            val adapter = MainRecyclerAdapter(requireContext(),it, this)
            recyclerView.adapter = adapter
            swipeLayout.isRefreshing = false //turning of refreshing indicator off.

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

    override fun onUserItemClick(user: GitHubUser) {
        Log.i(LOG_TAG, "Selected User: ${user.userSearchResult?.userName}")    }
}
