package com.priomkhan.githubsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.GitHubUser
import com.priomkhan.githubsearch.ui.shared.SharedViewModel

class MainFragment : Fragment(),
    MainRecyclerAdapter.UserItemListener {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout // References to the swiperefreshlayout of main_fragment.xml
    private lateinit var navController: NavController //New: Here I  declare an instance of the nav_controller class as a property of nav_graph fragment.

    private var count =0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        navController = Navigation.findNavController(
            requireActivity(),R.id.nav_host
        )

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

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
        Log.i(LOG_TAG, "Selected User: ${user.userSearchResult?.userName}")

        viewModel.selectedUser.value = user
        navController.navigate(R.id.action_nav_detail)

    }
}
