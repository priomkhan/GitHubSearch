package com.priomkhan.githubsearch.ui.details


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.priomkhan.githubsearch.LOG_TAG
import com.priomkhan.githubsearch.R
import com.priomkhan.githubsearch.ui.shared.SharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailedFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel //Copied from MainFragment:
    private lateinit var navController: NavController //Here is the ref that I copy from MainFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


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
        })


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed, container, false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            navController.navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }

}
