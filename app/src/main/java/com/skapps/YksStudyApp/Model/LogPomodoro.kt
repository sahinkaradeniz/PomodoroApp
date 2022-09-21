package com.skapps.YksStudyApp.Model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class LogPomodoro(
    @ColumnInfo(name = "acitivity")
    var activity: String?,
    @ColumnInfo(name = "time")
    var time: Int?,
    @ColumnInfo(name = "year")
    val year:String,
    @ColumnInfo(name = "month")
    val month:String,
    @ColumnInfo(name = "day")
    val day:String,
    @ColumnInfo(name = "week")
    val week:String,
    @ColumnInfo(name = "hour")
    val hour:String,
    @ColumnInfo(name = "minute")
    val minute:String):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}