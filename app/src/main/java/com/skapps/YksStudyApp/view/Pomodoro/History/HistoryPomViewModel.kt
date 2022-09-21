package com.skapps.YksStudyApp.view.Pomodoro.History

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Model.LogPomodoro
import com.skapps.YksStudyApp.database.LogPomDatabase
import kotlinx.coroutines.launch

class HistoryPomViewModel(application: Application) :BaseViewModel(application) {
    val logPomodorolist=MutableLiveData<List<LogPomodoro>>()
    val dao=LogPomDatabase(getApplication()).logPomDao()


    fun getLogPomodoroRoom(){
        launch {
            logPomodorolist.value=dao.getAllHistory()
        }
    }
}