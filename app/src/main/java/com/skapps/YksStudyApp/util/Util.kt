package com.skapps.YksStudyApp.util

import android.annotation.SuppressLint
import android.app.DownloadManager.COLUMN_ID
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.room.RoomMasterTable.TABLE_NAME
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


private lateinit var calendar: Calendar

@RequiresApi(Build.VERSION_CODES.O)
fun getTime() {
    fun now(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }
    val currentYear = SimpleDateFormat("yyyy",Locale.getDefault()).format(Date())
    val currentMonth = SimpleDateFormat("MM",Locale.getDefault()).format(Date())
    val currentDay = SimpleDateFormat("dd",Locale.getDefault()).format(Date())
    val currentHour = SimpleDateFormat("HH",Locale.getDefault()).format(Date())
    val currentminute = SimpleDateFormat("mm",Locale.getDefault()).format(Date())
    Log.e("Util.date",currentHour)
    Log.e("Util.date",now())
}


@SuppressLint("Range")
fun timepic(){
    val picker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setMinute(10)
        .setTitleText("Select Appointment time")
        .build()
    //picker.show(requireActivity().supportFragmentManager, picker.toString())
    picker.addOnPositiveButtonClickListener {
        calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = picker.hour
        calendar[Calendar.MINUTE] = picker.minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] =0
        val hour=picker.hour
        var total=hour*60*1000*1000*60
        val minute=picker.minute
    }

}