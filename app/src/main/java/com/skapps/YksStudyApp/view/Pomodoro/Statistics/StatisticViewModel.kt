package com.skapps.YksStudyApp.view.Pomodoro.Statistics

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Statistics.Pomodoro.PomodoroStatistics
import kotlinx.coroutines.launch

class StatisticViewModel(application: Application) :BaseViewModel(application) {
   private val pomodoroStatistics=PomodoroStatistics(getApplication())
    val dataBarEntry= MutableLiveData<ArrayList<BarEntry>>()
    val dataLineEntry= MutableLiveData<ArrayList<Entry>>()
    val dataPieEntry=MutableLiveData<ArrayList<PieEntry>>()
  fun getDataBarEntry(){
     launch {
        dataBarEntry.value=pomodoroStatistics.getStatisticsBar()
     }
  }
    fun getDataLineEntry(){
        launch {
           dataLineEntry.value=pomodoroStatistics.getStatisticLine()
        }
    }
    fun getDataPieEntry(){
        launch {
            dataPieEntry.value=pomodoroStatistics.getStatisticPie()
        }
    }


}