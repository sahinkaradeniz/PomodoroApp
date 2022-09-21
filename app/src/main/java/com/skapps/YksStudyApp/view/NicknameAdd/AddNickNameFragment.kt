package com.skapps.YksStudyApp.view.NicknameAdd

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.skapps.YksStudyApp.databinding.FragmentAddNickNameBinding
import com.skapps.YksStudyApp.view.Pomodoro.PomodoroActivity

class AddNickNameFragment:DialogFragment() {
    private lateinit var viewModel: AddNickNameViewModel
    private lateinit var  _binding:FragmentAddNickNameBinding
    private val binding get() = _binding
    private var nickname=String()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding=FragmentAddNickNameBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(AddNickNameViewModel::class.java)
        viewModel.getAllNickname()
        observeLiveData()
        return binding.root
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val xbinding = FragmentAddNickNameBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = false
        builder.setView(xbinding.root)
        val dialog = builder.create()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        xbinding.textnickname.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.duplicate(s.toString(),xbinding,requireContext())
                nickname=s.toString()
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
        xbinding.savenick.setOnClickListener {
            if (nickname.isNotEmpty()){
                viewModel.profileUpdates(nickname,requireContext())
                val intent = Intent(requireContext(), PomodoroActivity::class.java)
                startActivity(intent)
            }
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun observeLiveData(){
        viewModel.clickEnabled.observe(viewLifecycleOwner){
            binding.savenick.isClickable = it
        }
    }


}