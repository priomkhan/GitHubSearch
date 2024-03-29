package com.priomkhan.githubsearch.ui.main

import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
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

class MainFragment : Fragment(),
    MainRecyclerAdapter.UserItemListener {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout // References to the swiperefreshlayout of main_fragment.xml
    private lateinit var navController: NavController //Here I  declare an instance of the nav_controller class as a property of nav_graph fragment.
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        navController = Navigation.findNavController(
            requireActivity(),R.id.nav_host
        )

        progressBar = view.findViewById(R.id.progressBar) as ProgressBar

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

        viewModel.busyBool.observe(this, Observer {

            if(it){
                progressBar.visibility = View.VISIBLE;
              //  recyclerView.visibility = View.GONE
            }else{

                progressBar.visibility = View.INVISIBLE;
               // recyclerView.visibility = View.VISIBLE
               recyclerView.adapter?.notifyDataSetChanged();
            }

        })


        setHasOptionsMenu(true)

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){

        inflater.inflate(R.menu.main_menubar,menu)
        val menuItem = menu.findItem(R.id.tb_logout)

        val btn_LogOut = menuItem.actionView as Button
        btn_LogOut.setText("LOG OUT")
        btn_LogOut.setBackgroundColor(Color.RED)
        btn_LogOut.setOnClickListener{
            viewModel.logout()
            navController.navigateUp()
        }



    }


}
