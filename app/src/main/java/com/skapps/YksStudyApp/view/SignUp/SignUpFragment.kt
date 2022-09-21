package com.skapps.YksStudyApp.view.SignUp

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.databinding.FragmentSignUpBinding
import com.skapps.YksStudyApp.util.infoToast
import com.skapps.YksStudyApp.util.toast
import com.skapps.YksStudyApp.util.warningToast
import com.skapps.YksStudyApp.view.NicknameAdd.AddNickNameFragment
import es.dmoral.toasty.Toasty
import es.dmoral.toasty.Toasty.info

class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var _binding:FragmentSignUpBinding
    private val binding get() = _binding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.facebookLogin2.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_addNickNameFragment)
        }
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        observeLiveData()
        binding.sinupButton.setOnClickListener {
            val password = binding.signpasswordText.editText?.text.toString()
            val email = binding.signemailtext.editText?.text.toString()
            val name =binding.signupName.editText?.text.toString()
            if (name.isEmpty()){
               requireContext().warningToast("Lütfen isminizi yazınız")
            }else if (password.length<7){
               requireContext().warningToast("Şifre uzunluğu 6 karakterden kısa olamaz")
            }else if(viewModel.isValidEmail(email)){
               requireContext().warningToast("Email biçimi hatalı")
            }else{
              //  viewModel.emailControl(emailList,email,requireContext())
              //  requireContext().warningToast("Bu email başka bir hesap tarafından kullanılmakta")
              viewModel.userRegister(email,password,name,requireContext())
            }
        }
    }

    private fun observeLiveData(){
        viewModel.isSuccesful.observe(viewLifecycleOwner){
            if (it){
                findNavController().navigate(R.id.action_signUpFragment_to_addNickNameFragment)
            }
        }
   }
}