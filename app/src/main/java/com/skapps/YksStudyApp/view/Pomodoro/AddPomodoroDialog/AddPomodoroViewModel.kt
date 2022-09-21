package com.skapps.YksStudyApp.view.Pomodoro.AddPomodoroDialog

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Model.Pomodoro
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.database.PomodoroDatabase
import kotlinx.coroutines.launch

class AddPomodoroViewModel(application: Application):BaseViewModel(application) {

    val pomodoroList = MutableLiveData<List<Pomodoro>>()
    val dao = PomodoroDatabase(getApplication()).pomodoroDao()
  //  val pomodoroAllItem: LiveData<MutableList<HistoryPomodoro>> = pomodoroRepository.allHistoryItems.asLiveData()
  fun storeInRoom(pomodoro: Pomodoro) {
      var k=0
      if (pomodoroList.value != null) {
            for (i in pomodoroList.value!!){
                if (i.activity == pomodoro.activity && i.time == pomodoro.time){
                    k++
                }
            }
          if (k==0){
              launch {
                  dao.insert(pomodoro)
              }
          }
      }
  }
      fun onRadioButtonClicked(checkId: Int, context: Context): String {
          var activity: String = "Diger"
          when (checkId) {
              R.id.rdiger -> {
                  activity = "Diğer"
              }
              R.id.rkitap -> {
                  activity = "Kitap"
              }
              R.id.rdeneme -> {
                  activity = "Deneme"
              }
              R.id.rkonuanlatim -> {
                  activity = "Konu Anlatımı"
              }
              R.id.rsoru -> {
                  activity = "Soru Çözümü"
              }
              R.id.rkonutekrar -> {
                  activity = "Konu Tekrar"
              }
          }
          return activity
      }
    fun getDataRoom() {
        launch {
            pomodoroList.value= PomodoroDatabase(getApplication()).pomodoroDao().getAllHistory()
        }
    }


}


