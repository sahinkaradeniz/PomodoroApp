package com.skapps.YksStudyApp.view.Login

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.Statistics.Pomodoro.RatingPomodoro
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.databinding.FragmentLoginBinding
import com.skapps.YksStudyApp.view.NicknameAdd.AddNickNameFragment
import kotlinx.coroutines.DelicateCoroutinesApi

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding?=null
    private val binding get()=_binding
    @OptIn(DelicateCoroutinesApi::class)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = Firebase.auth
        viewModel= ViewModelProvider(this).get(LoginViewModel::class.java)
        _binding= FragmentLoginBinding.inflate(inflater,container,false)
        checkUser()
        binding!!.loginButton.setOnClickListener {
            val password=binding!!.passwordText.editText!!.text.toString()
            val email=binding!!.emailtext.editText!!.text.toString()
            Toast.makeText(requireContext(), email,Toast.LENGTH_SHORT).show()
            viewModel.loginUser(password,email,requireContext())
        }
        binding!!.facebookLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        observeLiveData()

        //checkUser()
        return  binding?.root
    }
   private fun checkUser(){
        val currentUser = auth.currentUser
        if(currentUser != null){
          findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }


   private fun observeLiveData(){
        viewModel.loginsuccesful.observe(viewLifecycleOwner){
            if (it){
                checkUser()
              //  showDialog()
            }
        }
    }
}