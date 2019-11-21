package com.priomkhan.githubsearch.ui.details


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.data.UserRepo
import com.priomkhan.githubsearch.databinding.DetailedFragmentBinding
import com.priomkhan.githubsearch.ui.shared.SharedViewModel
import kotlinx.android.synthetic.main.detailed_fragment.view.*

/**
 * A simple [Fragment] subclass.
 */
class DetailedFragment : Fragment(),DetailedRepoRecyclerAdapter.RepoItemListener, SearchView.OnQueryTextListener {



    private lateinit var viewModel: SharedViewModel //Copied from MainFragment:
    private lateinit var navController: NavController //Here is the ref that I copy from MainFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var repoList: List<UserRepo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).run {

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }

        val view = inflater.inflate(R.layout.detailed_fragment, container, false)
        recyclerView = view.findViewById(R.id.repoRecyclerView)
        /*
        In a fragment we have to add this line of code more to make the back button work.
         */
        setHasOptionsMenu(true)

        /*
        Here I am initializing nevController and then we need to add above code to make it work.
         */
        navController = Navigation.findNavController(
            requireActivity(),R.id.nav_host
        )

        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

        viewModel.selectedUser.observe(this, Observer {
            Log.i(LOG_TAG, "Selected User Name: ${it.userDetails?.name}")

            if(it.userRepoList!=null){
                repoList = it.userRepoList
                Log.i(LOG_TAG, "Number of Repository Found: ${it.userRepoList.size}")
                val adapter = DetailedRepoRecyclerAdapter(requireContext(),it.userRepoList,this)
                viewModel.adapter = adapter
                //recyclerView.adapter = adapter
            }else{
                Log.i(LOG_TAG, "No Public Repository Found")
            }

        })


        setHasOptionsMenu(true)
        /*
        When you use the data binding architecture, each layout that's set up for data binding
        will generate a special class. The name of the class will match the name of the layout
        but without any spaces or special characters. So detail_fragment.XML generates a class
        named DetailFragmentBinding.
        */

        val binding = DetailedFragmentBinding.inflate(
            inflater, container, false
        )
        binding.lifecycleOwner = this


        binding.viewModel = viewModel

        return binding.root


       //return view

    }

    //Go back to main menu on back button click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            navController.navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onUserItemClick(repo: UserRepo) {
        Log.i(LOG_TAG, "Selected Repository: ${repo.repoName}")
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(repo.html_url)
        startActivity(openURL)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
