package com.skapps.YksStudyApp.view.Pomodoro.RatingPomodoro

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skapps.YksStudyApp.Adapter.RatingPomodoroAdapter
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.databinding.FragmentRatingPomodoroBinding
import com.stone.vega.library.VegaLayoutManager


class RatingPomodoroFragment : Fragment() {

    private lateinit var viewModel: RatingPomodoroViewModel
    private var _binding:FragmentRatingPomodoroBinding?=null
    private val binding get() = _binding
    private val ratingPomodoroAdapter=RatingPomodoroAdapter(arrayListOf())
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=FragmentRatingPomodoroBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(RatingPomodoroViewModel::class.java)
        viewModel.getRating()
        binding?.pomodororatingrcv?.layoutManager=VegaLayoutManager()
        binding?.pomodororatingrcv?.adapter=ratingPomodoroAdapter
        binding?.pomodororatingrcv?.setHasFixedSize(true)
        observeLiveData()

        return binding?.root
    }

      private fun observeLiveData(){
          viewModel.ratingList.observe(viewLifecycleOwner){
              ratingPomodoroAdapter.updatePomodoroRating(it)
          }
    }

    override fun onResume() {
        observeLiveData()
        super.onResume()
    }
}