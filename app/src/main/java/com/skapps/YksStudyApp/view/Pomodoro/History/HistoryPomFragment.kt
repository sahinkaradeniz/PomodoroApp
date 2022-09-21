package com.skapps.YksStudyApp.view.Pomodoro.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skapps.YksStudyApp.Adapter.LogPomodoroAdapter
import com.skapps.YksStudyApp.databinding.FragmentHistoryPomBinding
import com.stone.vega.library.VegaLayoutManager


class HistoryPomFragment : Fragment() {
    private var _binding:FragmentHistoryPomBinding?=null
    private val binding get() = _binding
    private lateinit var viewModel: HistoryPomViewModel
    private val logPomodoroAdapter=LogPomodoroAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedIvnstanceState: Bundle?): View? {
        _binding= FragmentHistoryPomBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(HistoryPomViewModel::class.java)
        viewModel.getLogPomodoroRoom()
    //    binding?.logpomodororec?.layoutManager=LinearLayoutManager(context)
        binding?.logpomodororec?.layoutManager = VegaLayoutManager()
        binding?.logpomodororec?.adapter=logPomodoroAdapter
        binding?.logpomodororec?.setHasFixedSize(true)
        observeLivedata()
        return binding?.root
    }

    override fun onResume() {
        observeLivedata()
        super.onResume()
    }
    private fun observeLivedata(){
        viewModel.logPomodorolist.observe(viewLifecycleOwner){
            logPomodoroAdapter.updateLogPomodoro(it)
        }
    }
}