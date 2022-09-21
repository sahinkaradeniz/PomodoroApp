package com.skapps.YksStudyApp.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skapps.YksStudyApp.Model.LogPomodoro
import java.time.Month

@Dao
interface LogPomDao{
    @Insert
    suspend fun insert(history:LogPomodoro)
    @Query("SELECT * FROM logpomodoro")
    suspend fun getAllHistory():List<LogPomodoro>
    @Query("Select * From logpomodoro Where year= :year and month=:month")
    suspend fun getMonthData(year:String,month:String):List<LogPomodoro>
    @Query("Select * From logpomodoro Where year= :year and week=:week")
    suspend fun getWeekData(year:String,week:String):List<LogPomodoro>
    @Delete
    suspend fun deleteHistory(historyItem:LogPomodoro)
}