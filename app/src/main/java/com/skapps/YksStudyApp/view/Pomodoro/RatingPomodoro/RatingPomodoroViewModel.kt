package com.skapps.YksStudyApp.view.Pomodoro.RatingPomodoro

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Model.UserProfile
import kotlinx.coroutines.launch

class RatingPomodoroViewModel(application: Application): BaseViewModel(application) {
   private val db= Firebase.firestore
    var ratingList=MutableLiveData<ArrayList<UserProfile>>()
    fun getRating(){
        val userList=ArrayList<UserProfile>()
        launch {
            db.collection("users").orderBy("totaltime",Query.Direction.DESCENDING)
                .addSnapshotListener { document, e ->
                    if (e != null) {
                        Log.e("getRating", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    userList.clear()
                    for (value in document!!) {
                        val id=value.get("id").toString()
                        val nickname=value.get("nickname").toString()
                        val email=value.get("email").toString()
                        val password=value.get("password").toString()
                        val name=value.get("name").toString()
                        val totaltime=value.get("totaltime") as Long
                        val user=UserProfile(id,nickname,email,password,name,totaltime)
                        userList.add(user)
                        Log.e("getRating", "Current cites in CA: ${value.get("totaltime")}")
                    }
                    ratingList.value=userList
                }
        }
    }


}