package com.skapps.YksStudyApp.view.SignUp

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Model.UserProfile
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.util.warningToast
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    private  var auth: FirebaseAuth
    var isSuccesful=MutableLiveData<Boolean>()
    val db= Firebase.firestore
    init {
        auth=Firebase.auth
    }

    fun userRegister(email:String,password:String,name: String,context: Context){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    isSuccesful.value=true
                    saveUser(email,password,name,auth.currentUser!!.uid,context)
                    Log.e(TAG,"createUserWithEmail:success")
                } else {
                    Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
                    isSuccesful.value=false
                    Log.w(TAG, "createUserWithEmail:failure")
                    context.warningToast(task.exception?.message.toString())
                }
            }
         }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
                false
             } else {
            !Patterns.EMAIL_ADDRESS.matcher(target).matches()
          }
       }

    fun saveUser(email:String,password:String,name:String,uid:String,context: Context,nick:String="null"){
        launch {
            val local =LocalDatabase()
            val user=UserProfile(uid,nick,email,password, name,0)
            db.collection("users").document(uid).set(user).addOnSuccessListener { documentReference ->
                Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                local.setSharedPreference(context,"username",name)
                local.setSharedPreference(context,"userid",uid)
            }.addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error adding document", e)
            }
        }
    }
}


