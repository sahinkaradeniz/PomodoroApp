package com.skapps.YksStudyApp.dao

import androidx.room.*
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.skapps.YksStudyApp.Model.Pomodoro

@Dao
interface PomodoroDao {
    @Insert
    suspend fun insert(history:Pomodoro)
    @Query("SELECT * FROM pomodoro")
    suspend fun getAllHistory():List<Pomodoro>
    @Delete
    suspend fun deleteHistory(historyItem: Pomodoro)
}