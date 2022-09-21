package com.skapps.YksStudyApp.Statistics.Pomodoro

import android.app.Application
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import com.skapps.YksStudyApp.Model.LogPomodoro
import com.skapps.YksStudyApp.database.LogPomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PomodoroStatistics(application: Application){
    val calender = Calendar.getInstance()
    val currentYear = SimpleDateFormat("yyyy",Locale.getDefault()).format(Date())
    val currentMonth = SimpleDateFormat("MM",Locale.getDefault()).format(Date())
    val currentWeek =calender.get(Calendar.WEEK_OF_YEAR).toString()
    val currentDay= calender.get(Calendar.DAY_OF_MONTH).toString()
    val database=LogPomDatabase(application).logPomDao()
    val days=Days(0,0,0,0,0,0,0)
    var logList=ArrayList<LogPomodoro>()
    var logListWeek =ArrayList<LogPomodoro>()
    val totalActivity=TotalActivity(0,0,0,0,0,0)
    init {
        getAllLog()
    }
     fun getAllLog(){
         GlobalScope.launch {
             logList= database.getAllHistory() as ArrayList<LogPomodoro>
             logListWeek= database.getWeekData(currentYear,currentWeek) as ArrayList<LogPomodoro>
         }
    }
    suspend fun getStatisticsBar():ArrayList<BarEntry>{
        val barEntry= arrayListOf<BarEntry>()
        for (i in logList){
            when(i.activity){
                "Konu Anlatımı" -> {totalActivity.subjectA+= i.time!! }
                "Konu Tekrar" -> {totalActivity.subjectT+= i.time!! }
                "Soru Çözümü" -> {totalActivity.question+= i.time!! }
                "Deneme" -> {totalActivity.test+= i.time!! }
                "Kitap" -> {totalActivity.book+= i.time!! }
                "Diğer" -> {totalActivity.other+= i.time!!}
            }
        }
            barEntry.add(BarEntry(0f,totalActivity.subjectA.toFloat()))
            barEntry.add(BarEntry(1f,totalActivity.question.toFloat()))
            barEntry.add(BarEntry(2f,totalActivity.subjectT.toFloat()))
            barEntry.add(BarEntry(3f,totalActivity.test.toFloat()))
            barEntry.add(BarEntry(4f,totalActivity.book.toFloat()))
            barEntry.add(BarEntry(5f,totalActivity.other.toFloat()))
        return barEntry
    }


    suspend fun getStatisticPie():ArrayList<PieEntry>{
        val pieEntry = arrayListOf<PieEntry>()
        if (totalActivity.other !=0){
            pieEntry.add(PieEntry(totalActivity.other.toFloat(),"Diğer"))
        }
        if (totalActivity.subjectA !=0){
            pieEntry.add(PieEntry(totalActivity.subjectA.toFloat(),"Konu A."))
        }
        if (totalActivity.subjectT !=0){
            pieEntry.add(PieEntry(totalActivity.subjectT.toFloat(),"Konu T."))
        }
        if (totalActivity.question !=0){
            pieEntry.add(PieEntry(totalActivity.question.toFloat(),"Soru"))
        }
        if (totalActivity.book !=0){
            pieEntry.add(PieEntry(totalActivity.book.toFloat(),"Kitap O."))
        }
        if (totalActivity.test !=0){
            pieEntry.add(PieEntry(totalActivity.test.toFloat(),"Deneme"))

        }

        return pieEntry
    }


    suspend fun getStatisticLine():ArrayList<Entry>{
        when(currentDay.toInt()){
            in 1..7 -> {
                for (i in logListWeek){
                    when(i.day.toInt()){
                        1 -> days.g1 +=i.time!!.toInt()
                        2 -> days.g2 +=i.time!!.toInt()
                        3 -> days.g3 +=i.time!!.toInt()
                        4 -> days.g4 +=i.time!!.toInt()
                        5 -> days.g5 +=i.time!!.toInt()
                        6 -> days.g6 +=i.time!!.toInt()
                        7 -> days.g7 +=i.time!!.toInt()
                    }
                }
            }
            in 8..14 -> {
                for (i in logListWeek){
                    when(i.day.toInt()){
                         8 -> days.g1 +=i.time!!.toInt()
                         9 -> days.g2 +=i.time!!.toInt()
                        10 -> days.g3 +=i.time!!.toInt()
                        11 -> days.g4 +=i.time!!.toInt()
                        12 -> days.g5 +=i.time!!.toInt()
                        13 -> days.g6 +=i.time!!.toInt()
                        14 -> days.g7 +=i.time!!.toInt()
                    }
                }
            }
            in 15..21->{
                for (i in logListWeek){
                    when(i.day.toInt()){
                        15 -> days.g1 +=i.time!!.toInt()
                        16 -> days.g2 +=i.time!!.toInt()
                        17 -> days.g3 +=i.time!!.toInt()
                        18 -> days.g4 +=i.time!!.toInt()
                        19 -> days.g5 +=i.time!!.toInt()
                        20 -> days.g6 +=i.time!!.toInt()
                        21 -> days.g7 +=i.time!!.toInt()
                    }
                }
            }
            in 22..30 -> {
                for (i in logListWeek){
                    when(i.day.toInt()){
                        22 -> days.g1 +=i.time!!.toInt()
                        23 -> days.g2 +=i.time!!.toInt()
                        24 -> days.g3 +=i.time!!.toInt()
                        25 -> days.g4 +=i.time!!.toInt()
                        26 -> days.g5 +=i.time!!.toInt()
                        27 -> days.g6 +=i.time!!.toInt()
                        28 -> days.g7 +=i.time!!.toInt()
                    }
                }
            }
        }
        val linevalues = ArrayList<Entry>()
        linevalues.add(Entry(1f, days.g1.toFloat()))
        linevalues.add(Entry(2f, days.g2.toFloat()))
        linevalues.add(Entry(3f, days.g3.toFloat()))
        linevalues.add(Entry(4f, days.g4.toFloat()))
        linevalues.add(Entry(5f, days.g5.toFloat()))
        linevalues.add(Entry(6f, days.g6.toFloat()))
        linevalues.add(Entry(7f, days.g7.toFloat()))

        return linevalues
    }


}
















