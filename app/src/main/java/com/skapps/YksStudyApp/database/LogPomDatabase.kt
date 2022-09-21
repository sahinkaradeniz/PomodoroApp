package com.skapps.YksStudyApp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.skapps.YksStudyApp.Model.LogPomodoro
import com.skapps.YksStudyApp.dao.LogPomDao


@Database(entities = arrayOf(LogPomodoro::class),version = 4, exportSchema = false)
abstract class LogPomDatabase : RoomDatabase() {

    abstract fun logPomDao() : LogPomDao

    //Singleton

    companion object {

        @Volatile private var instance :LogPomDatabase? = null

        private val lock = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }


        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,LogPomDatabase::class.java,"logPomodorodatabase"
        ).build()
    }
}