package com.start.STart.ui.home.event

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.banner.Event
import com.start.STart.databinding.ItemEventBinding

class EventAdapter() : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){
    var list: List<Event> = mutableListOf()

        set(value) {
            field = value
        }

    inner class EventViewHolder(var binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event){
            binding.textEventName.text = event.title
            binding.textDate.text = event.startTime.subSequence(5, 7).toString() + "/" + event.startTime.subSequence(8,10) + " ~ " + event.startTime.subSequence(5, 7).toString() + "/" + event.startTime.subSequence(8,10)

            Glide.with(binding.eventImg.context)
                .load(event.imageUrl)
                .centerCrop()
                .into(binding.eventImg)

            val purple_ghost = ContextCompat.getColor(binding.textEventName.context, R.color.dream_purple_ghost)
            val purple = ContextCompat.getColor(binding.textEventName.context, R.color.dream_purple)
            val gray = ContextCompat.getColor(binding.textEventName.context, R.color.dream_gray)
            val navy = ContextCompat.getColor(binding.textEventName.context, R.color.dream_navy)

            when(event.eventStatus) {
                "PROCEEDING" -> {
                    binding.eventStatus.backgroundTintList = ColorStateList.valueOf(purple)
                    binding.statusText.text = "진행중"

                    binding.textEventName.setTextColor(purple)
                    binding.textDate.setTextColor(navy)
                }
                "END" -> {
                    binding.eventStatus.backgroundTintList = ColorStateList.valueOf(gray)
                    binding.statusText.text = "종료"

                    binding.textEventName.setTextColor(gray)
                    binding.textDate.setTextColor(gray)
                }
                "BEFORE" -> {
                    binding.eventStatus.backgroundTintList = ColorStateList.valueOf(purple_ghost)
                    binding.statusText.text = "진행\n예정"

                    binding.textEventName.setTextColor(purple_ghost)
                    binding.textDate.setTextColor(purple)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size


}