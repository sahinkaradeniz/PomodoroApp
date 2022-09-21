package com.skapps.YksStudyApp.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skapps.YksStudyApp.Model.UserProfile
import com.skapps.YksStudyApp.databinding.CardPomodoroRatingBinding

class RatingPomodoroAdapter(var userlist:ArrayList<UserProfile>):RecyclerView.Adapter<RatingPomodoroAdapter.RatingViewHolder>(){
    class RatingViewHolder(val recyclerRowBinding:CardPomodoroRatingBinding):RecyclerView.ViewHolder(recyclerRowBinding.root) {
        fun bind(userProfile: UserProfile){
            recyclerRowBinding.pomodoroRatingName.text=userProfile.name
            recyclerRowBinding.Ratingtime.text="${userProfile.totaltime} dk"
            recyclerRowBinding.pmRatingNick.text=userProfile.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
       val recyclerRowBinding=CardPomodoroRatingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  RatingViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        Log.e("onbind",userlist.get(position).name)
      holder.bind(userlist.get(position))
    }

    override fun getItemCount(): Int {
      return userlist.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updatePomodoroRating(newuserlist:List<UserProfile>){
        userlist.clear()
        userlist.addAll(newuserlist)
        notifyDataSetChanged()
    }
}