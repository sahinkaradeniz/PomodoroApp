package com.skapps.YksStudyApp.view.Pomodoro

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.database.LocalDatabase

class PomodoroViewModel(application: Application):BaseViewModel(application) {
    companion object{
        private const val CHART_LABEL="DAY_CHART"
    }
    private  var localDatabase= LocalDatabase()
     var time = MutableLiveData<Long>()
    var cTimer: CountDownTimer? = null
    var isCounterRunning = false
    private val dayData = mutableListOf<Entry>()
     private val _lineDataSet=MutableLiveData(LineDataSet(dayData, CHART_LABEL))
    val lineDataSet:LiveData<LineDataSet> =_lineDataSet
    init {
        dayData.add(Entry(0f,5f))
        dayData.add(Entry(1f,3f))
        dayData.add(Entry(2f,4f))
        dayData.add(Entry(3f,5f))
        dayData.add(Entry(4f,6f))
        dayData.add(Entry(5f,9f))
        dayData.add(Entry(6f,1f))
        dayData.add(Entry(7f,5f))
        dayData.add(Entry(8f,3f))
        dayData.add(Entry(9f,9f))
        _lineDataSet.value= LineDataSet(dayData, CHART_LABEL)
    }

    fun startTimer(minute :Long,context: Context){
        localDatabase= LocalDatabase()
        cTimer = object : CountDownTimer(minute, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time.value=millisUntilFinished
            }
            override fun onFinish() {
               localDatabase.removeSharedPreference(context,"pause")
            }
        }
        (cTimer as CountDownTimer).start()
    }





    private fun yourOperation() {
        if (!isCounterRunning) {
            isCounterRunning = true
            cTimer?.start()
        } else {
            cTimer?.cancel() // cancel
            cTimer?.start() // then restart
        }
    }
    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }
    fun onCreatePause(context: Context,pauseTime:Int){
        cancelTimer()
        if (pauseTime != 0){
            startTimer(pauseTime.toLong(),context.applicationContext)
        }else{
            startTimer(25*1000*60,context.applicationContext)
        }
    }
}