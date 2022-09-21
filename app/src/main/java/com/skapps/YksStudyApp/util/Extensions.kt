package com.skapps.YksStudyApp.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import es.dmoral.toasty.Toasty


@SuppressLint("CheckResult")
infix fun Context.infoToast(message:String){
    Toasty.info(this,message, Toasty.LENGTH_SHORT).show()
}

@SuppressLint("CheckResult")
infix fun Context.toast(message:String){
    Toasty.normal(this,message, Toasty.LENGTH_SHORT).show()
}

@SuppressLint("CheckResult")
infix fun Context.warningToast(message:String){
    Toasty.warning(this,message, Toasty.LENGTH_SHORT).show()
}
@SuppressLint("CheckResult")
infix fun Context.succesToast(message:String){
    Toasty.success(this,message, Toasty.LENGTH_SHORT).show()
}

fun NavController.safeNavigate(direction: NavDirections) {
    Log.d("safe", "Click happened")
    currentDestination?.getAction(direction.actionId)?.run {
        Log.d("safe", "Click Propagated")
        navigate(direction)
    }
}
