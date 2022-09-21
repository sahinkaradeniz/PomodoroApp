package com.skapps.YksStudyApp.Listener

import android.view.View
import com.skapps.YksStudyApp.Model.Pomodoro

interface HistoryClickListener {
    fun onHistoryClicked(pomodoro: Pomodoro)
}