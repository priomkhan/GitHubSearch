package com.priomkhan.githubsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
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
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(),
    MainRecyclerAdapter.UserItemListener {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout // References to the swiperefreshlayout of main_fragment.xml
    private lateinit var navController: NavController //Here I  declare an instance of the nav_controller class as a property of nav_graph fragment.


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



        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        viewModel.ifLocalUserExist.observe(this, Observer {
            viewModel.refreshData()
        })

        viewModel.userOnlineData.observe(this, Observer {

            val adapter = MainRecyclerAdapter(requireContext(),it, this)
            recyclerView.adapter = adapter
            swipeLayout.isRefreshing = false //turning of refreshing indicator off.

        })

        //Logout button
        val btn_Logout = view.findViewById(R.id.btn_Logout) as Button
        btn_Logout.setOnClickListener {
            viewModel.logout()
            navController.navigateUp()

        }


        val searchView = view.findViewById(R.id.sv_SearchUser) as SearchView

        searchView.setQueryHint("Search for GitHub User")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val newQurey = query?:""
                Log.i(LOG_TAG," MainFragment: New Query Search: ${newQurey}")
                viewModel.search(newQurey)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //viewModel.search(newText?:"")
                return false
            }

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
        navController.navigate(R.id.nav_to_detail)

    }


}
