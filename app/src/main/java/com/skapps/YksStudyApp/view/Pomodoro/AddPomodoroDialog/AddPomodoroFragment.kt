package com.skapps.YksStudyApp.view.Pomodoro.AddPomodoroDialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skapps.YksStudyApp.Listener.HistoryClickListener
import com.skapps.YksStudyApp.Adapter.HistoryPomodoroAdapter
import com.skapps.YksStudyApp.Model.Pomodoro
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.databinding.FragmentAddPomodoroBinding
import com.skapps.YksStudyApp.view.Pomodoro.PomodoroActivity
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams


class AddPomodoroFragment : BottomSheetDialogFragment(), HistoryClickListener {

    private var _binding:FragmentAddPomodoroBinding?=null
    private val binding get() = _binding
    private lateinit var viewModel: AddPomodoroViewModel
    private  var time:Int=0
    private var logActivity:String="Diğer"
    private val pomodoroAdapter=HistoryPomodoroAdapter(arrayListOf(),this)
    private var addhistory= Pomodoro("Diğer",25)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel= ViewModelProvider(this).get(AddPomodoroViewModel::class.java)
        viewModel.getDataRoom()
        _binding=FragmentAddPomodoroBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.AppBottomSheetDialogTheme)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.myRecyclerView?.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding?.myRecyclerView?.adapter=pomodoroAdapter

        viewModel = ViewModelProvider(this).get(AddPomodoroViewModel::class.java)
        observeLivedata()

        binding!!.seekBar.setOnSeekChangeListener(object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams) {
                time=seekParams.progress
                addhistory.time=seekParams.progress
            }
            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar) {}
            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
            }
        })
        binding!!.radioGroup.setOnCheckedChangeListener { group, checkedId ->
        addhistory.activity=viewModel.onRadioButtonClicked(checkedId,requireContext())
        logActivity=viewModel.onRadioButtonClicked(checkedId,requireContext())
        }
        binding!!.button.setOnClickListener {
            viewModel.storeInRoom(addhistory)
            val intent = Intent(requireContext(), PomodoroActivity::class.java)
            intent.putExtra("addpomodoro" ,time)
            intent.putExtra("activity",logActivity)
            startActivity(intent)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeLivedata(){
        viewModel.pomodoroList.observe(viewLifecycleOwner){
            pomodoroAdapter.updatePomodoro(it)
        }
    }

    private fun startPomodoro(time:Int){
        val intent = Intent(requireContext(), PomodoroActivity::class.java)
        intent.putExtra("addpomodoro" ,time)
        intent.putExtra("activity",logActivity)
        startActivity(intent)
    }

    override fun onHistoryClicked(pomodoro: Pomodoro) {
        logActivity= pomodoro.activity.toString()
        pomodoro.time?.let { startPomodoro(it) }
    }
}
