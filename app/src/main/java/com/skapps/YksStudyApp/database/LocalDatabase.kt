package com.skapps.YksStudyApp.database

import android.content.Context
import android.content.SharedPreferences

class LocalDatabase(){

    fun setSharedPreference(context: Context, key: String?, time: String) {
        val sharedPref: SharedPreferences = context.getSharedPreferences("com.skapps.YksStudyApp.database", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putString(key,time)
        edit.commit()
    }

    fun getSharedPreference(context: Context, key: String?, defaultValue: String?): String? {
        return context.getSharedPreferences("com.skapps.YksStudyApp.database", Context.MODE_PRIVATE)
            .getString(key, defaultValue!!)
    }

    fun clearSharedPreference(context: Context) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("com.skapps.YksStudyApp.database", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.clear()
        edit.commit()
    }

    fun removeSharedPreference(context: Context, key: String?) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("com.skapps.YksStudyApp.database", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.remove(key)
        edit.commit()
    }
}