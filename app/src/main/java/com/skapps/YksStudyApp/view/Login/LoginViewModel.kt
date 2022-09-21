package com.skapps.YksStudyApp.view.Login

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.database.LocalDatabase

import com.skapps.YksStudyApp.util.warningToast
import com.skapps.YksStudyApp.view.NicknameAdd.AddNickNameFragment


class LoginViewModel(application: Application):BaseViewModel(application) {

    var loginsuccesful= MutableLiveData<Boolean>()
    private var auth:FirebaseAuth = Firebase.auth
    fun loginUser(password:String,email:String,context: Context){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    loginsuccesful.value = true
                    // Sign in success, update UI with the signed-in user's information
                    Log.e(ContentValues.TAG,"signInWithEmail:success")
                    val user = auth.currentUser
                    saveuid(user!!.uid,context)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(task.exception.toString(), "signInWithEmail:failure")
                    task.exception?.toString()?.let { context.warningToast(it) }
                }
            }
    }

    fun saveuid(uid:String,context: Context){
        val local = LocalDatabase()
        local.setSharedPreference(context,"useruid",uid)
    }

}