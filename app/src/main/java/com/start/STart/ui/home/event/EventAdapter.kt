package com.start.STart.ui.home.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.databinding.ItemEventBinding

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>(){
    var list: List<Event> = listOf()
        set(value) {
            field = value
        }

    inner class EventViewHolder(var binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event){
            binding.textEventName.text = event.name
            binding.textDate.text = event.date
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