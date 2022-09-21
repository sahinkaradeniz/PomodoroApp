package com.skapps.YksStudyApp.view.NicknameAdd

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.skapps.YksStudyApp.Base.BaseViewModel
import com.skapps.YksStudyApp.Model.UserProfile
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.database.LocalDatabase
import com.skapps.YksStudyApp.databinding.FragmentAddNickNameBinding
import com.skapps.YksStudyApp.util.succesToast
import kotlinx.coroutines.launch
import java.lang.Exception

class AddNickNameViewModel(application: Application) :BaseViewModel(application) {
    private var auth: FirebaseAuth = Firebase.auth
    private val db= Firebase.firestore
    private var useruid=auth.currentUser?.uid
    private val localDatabase=LocalDatabase()
    private val dbFirestore = Firebase.firestore
    private val nicklist=ArrayList<String>()
    var clickEnabled=MutableLiveData<Boolean>()
    var nicknamelist=MutableLiveData<ArrayList<String>>()
    init {
        useruid= localDatabase.getSharedPreference(application.applicationContext,"useruid",useruid)
        getAllNickname()
    }
    fun getAllNickname(){
       dbFirestore.collection("users").addSnapshotListener { value, error ->
           Log.e("re","we")
           if(value !=null){
               val documents = value.documents
                   try {
                      for(document in documents){
                         val nick=document.get("nickname") as String
                          nicknamelist.value?.add(nick)
                          nicklist.add(nick)
                          Log.e("re",nick)
                      }
                   }catch (e:Exception) {
                       Log.e("getallnick",e.toString())
                   }
           }
       }
    }
    @SuppressLint("SetTextI18n")
    fun duplicate(nick:String, binding:FragmentAddNickNameBinding,context: Context){
            var cout=0
            if (nick.isNotEmpty()){
                for (i in nicklist){
                    if (i==nick){
                        cout+=1
                    }
                }
                if (cout>0){
                    clickEnabled.value=false
                    binding.textExplanation.text="Bu kullanıcı adı kullanılmaktadır."
                    binding.textExplanation.setTextColor(ContextCompat.getColor(context,R.color.darkred))
                }else{
                    clickEnabled.value=true
                    binding.textExplanation.text="Kullanılabilir"
                    binding.textExplanation.setTextColor(ContextCompat.getColor(context,R.color.black))
                }
            }else{
                clickEnabled.value=false
                binding.textExplanation.text="Kullanıcı adı boş olamaz"
                binding.textExplanation.setTextColor(ContextCompat.getColor(context,R.color.darkred))
            }
    }
    fun profileUpdates(nick: String,context: Context){
        launch {
            val user=Firebase.auth.currentUser
            val profileUpdates= userProfileChangeRequest {
                displayName=nick
            }
            user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    context.succesToast("Profil oluşturuldu")
                }
            }
        }
    }
}