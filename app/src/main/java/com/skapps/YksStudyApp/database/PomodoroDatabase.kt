package com.skapps.YksStudyApp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skapps.YksStudyApp.Model.Pomodoro
import com.skapps.YksStudyApp.dao.PomodoroDao


@Database(entities = arrayOf(Pomodoro::class),version = 2, exportSchema = false)
abstract class PomodoroDatabase : RoomDatabase() {

    abstract fun pomodoroDao() : PomodoroDao

    //Singleton

    companion object {

        @Volatile private var instance : PomodoroDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }


        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,PomodoroDatabase::class.java,"pomodorodatabase"
        ).build()
    }
}