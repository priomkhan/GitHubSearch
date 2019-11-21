package com.priomkhan.githubsearch.ui.login


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
class login_Fragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private lateinit var navController: NavController //Here I  declare an instance of the nav_controller class as a property of nav_graph fragment.
    private lateinit var et_user_name: EditText
    private lateinit var et_password: EditText
    private lateinit var btn_reset: Button
    private lateinit var btn_submit: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login_, container, false)

        et_user_name = view.findViewById(R.id.et_user_name) as EditText
        et_password = view.findViewById(R.id.et_password) as EditText
        btn_reset = view.findViewById(R.id.btn_reset) as Button
        btn_submit = view.findViewById(R.id.btn_submit)

        navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host
        )
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

//        val isExist = viewModel.checkIfUserExist()

//        if(isExist){
//            Toast.makeText(view.context, "User Exist Showing Result",Toast.LENGTH_LONG).show()
//            //viewModel.refreshData()
//            //navController.navigate(R.id.nav_to_mainFragment)
//        }
//
        viewModel.ifLocalUserExist.observe(this, Observer {

            if(it){
               navController.navigate(R.id.nav_to_mainFragment)
            }else{
                Log.i(LOG_TAG,"No Local User found. Need to Sign in." )
            }
       })

        //Clearing user_name and password on reset button click
        btn_reset.setOnClickListener {

            et_user_name.setText("")
            et_password.setText("")
        }

        btn_submit.setOnClickListener {
            if(et_user_name.text.isNotBlank() && et_password.text.isNotBlank()){
                val userName = et_user_name.text.toString()
                val password = et_password.text.toString()

                Toast.makeText(it.context, "Login: ${userName}  and ${password}",Toast.LENGTH_LONG).show()

                viewModel.login(userName, password)
                //viewModel.userLoginId = et_user_name.text.toString()
                //viewModel.password = et_password.text.toString()
                navController.navigate(R.id.nav_to_mainFragment)

            }else{
                Toast.makeText(it.context, "Please input your Github username and password",Toast.LENGTH_LONG).show()
            }

        }
        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login_, container, false)
    }


}
