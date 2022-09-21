package com.skapps.YksStudyApp.view.Home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.databinding.FragmentHomeBinding
import com.skapps.YksStudyApp.util.getTime
import com.skapps.YksStudyApp.view.Pomodoro.PomodoroActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding
    private lateinit var countDownTimer:CountDownTimer
    private val localDatabase=LocalDatabase()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=FragmentHomeBinding.inflate(inflater,container,false)
        printDifferenceDateForHours()
        binding!!.pomodoro.setOnClickListener {
            val intent = Intent(requireContext(), PomodoroActivity::class.java)
            startActivity(intent)
        }
        binding!!.analiz.setOnClickListener {

        }
       binding!!.homenameText.text= localDatabase.getSharedPreference(requireContext(),"username","null")


        return binding?.root
    }

    fun printDifferenceDateForHours() {
        val currentTime = Calendar.getInstance().time
        val endDateDay = "29/09/2022 00:00:00"
        val format1 = SimpleDateFormat("dd/MM/yyyy hh:mm:ss",Locale.getDefault())
        val endDate = format1.parse(endDateDay)
        //milliseconds
        var different = endDate.time - currentTime.time
        countDownTimer = object : CountDownTimer(different, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                binding?.dateYks?.text = "$elapsedDays Gün $elapsedHours Saat $elapsedMinutes Dakika $elapsedSeconds sn. kaldı."
            }

            override fun onFinish() {
                binding?.dateYks?.text = "Bugün Sınav"
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}