package com.skapps.YksStudyApp.Statistics.Pomodoro

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.Model.UserProfile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


@DelicateCoroutinesApi
class RatingPomodoro{
    private var auth: FirebaseAuth = Firebase.auth
    val db= Firebase.firestore
    private val dbFirestore = Firebase.firestore

    fun updateTimes(addminute: Number){
        GlobalScope.launch {
            getUser(addminute)
        }
    }


    fun getUser(addminute: Number){
        val uid=auth.currentUser?.uid
        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { value ->
                if (value != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${value.data}")
                    try {
                        val user=UserProfile(value.get("id").toString(),value.get("nickname").toString(),value.get("email").toString(),value.get("password").toString(),value.get("name").toString(),value.get("totaltime") as Number)
                        setUser(user,addminute)
                        Log.e("User",user.totaltime.toString())
                    }catch (e: Exception) {
                        Log.e("getUser",e.toString())
                        Log.e("getUser",uid.toString())
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
    fun setUser(userProfile: UserProfile,addminute: Number){
        GlobalScope.launch{
            var a=userProfile.totaltime
            Log.e("User",userProfile.totaltime.toString())
            a=a.toInt()+addminute.toInt()
            userProfile.totaltime=a
            db.collection("users").document(userProfile.id).set(userProfile).addOnSuccessListener {documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                Log.w(ContentValues.TAG, "Profile Update ! : ${userProfile}",)
            }.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
        }
    }

}