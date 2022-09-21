package com.skapps.YksStudyApp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skapps.YksStudyApp.Model.LogPomodoro
import com.skapps.YksStudyApp.databinding.CardhistorypomodoroBinding

class LogPomodoroAdapter(var historyList:ArrayList<LogPomodoro>):RecyclerView.Adapter<LogPomodoroAdapter.LogViewHolder>(){
    class LogViewHolder(val rowBinding:CardhistorypomodoroBinding):RecyclerView.ViewHolder(rowBinding.root){
        @SuppressLint("SetTextI18n")
        fun bind(logPomodoro: LogPomodoro){
            rowBinding.historyActivity.text=logPomodoro.activity
            rowBinding.historyTime.text=logPomodoro.time.toString()
            rowBinding.historycalenderdate.text="${logPomodoro.day}.${logPomodoro.month}.${logPomodoro.year}"
            rowBinding.historycalendertime.text="${logPomodoro.hour}:${logPomodoro.minute}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val rowBinding=CardhistorypomodoroBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LogViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(historyList.get(position))
    }

    override fun getItemCount(): Int {
       return historyList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateLogPomodoro(newLogPomdoroList: List<LogPomodoro>){
        historyList.clear()
        historyList.addAll(newLogPomdoroList)
        notifyDataSetChanged()
    }
}