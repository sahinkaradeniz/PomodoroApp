package com.skapps.YksStudyApp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skapps.YksStudyApp.Listener.HistoryClickListener
import com.skapps.YksStudyApp.Model.Pomodoro
import com.skapps.YksStudyApp.databinding.AddpomodorocardBinding

class HistoryPomodoroAdapter(var historyList:ArrayList<Pomodoro>,private val listener: HistoryClickListener) :RecyclerView.Adapter<HistoryPomodoroAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(val recyclerRowBinding:AddpomodorocardBinding):RecyclerView.ViewHolder(recyclerRowBinding.root){

            fun bind(pomodoro: Pomodoro,listener: HistoryClickListener){
                val time =pomodoro.time.toString()
                recyclerRowBinding.activity.text=pomodoro.activity
                recyclerRowBinding.time.text="$time dk"
                itemView.setOnClickListener {
                    listener.onHistoryClicked(pomodoro)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val recyclerRowBinding=AddpomodorocardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(
            recyclerRowBinding
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
         holder.bind(historyList.get(position),listener)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatePomodoro(newPomodoroList:List<Pomodoro>){
        historyList.clear()
        historyList.addAll(newPomodoroList)
        notifyDataSetChanged()
    }
}