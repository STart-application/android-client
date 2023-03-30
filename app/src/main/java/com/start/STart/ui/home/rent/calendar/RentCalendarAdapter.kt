package com.start.STart.ui.home.rent.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.databinding.ItemRentCalendarBinding
import java.util.Calendar

class RentCalendarAdapter: RecyclerView.Adapter<RentCalendarAdapter.RentCalendarViewHolder>() {

    // 선택된 날짜
    var userSelectedIndex = -1

    var list: List<RentDateItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class RentCalendarViewHolder(val binding: ItemRentCalendarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentCalendarViewHolder {
        val binding = ItemRentCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RentCalendarViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RentCalendarViewHolder, position: Int) {
        val item = list[position]

        holder.binding.let {
            it.textDate.text = item.date.get(Calendar.DATE).toString()
            if(item.total == 0 || item.count == 0) {
                it.progressbar.visibility = View.INVISIBLE
            } else {
                it.progressbar.visibility = View.VISIBLE

                val percent = ((item.count.toFloat() / item.total) * 100).toInt()
                Log.d(null, "onBindViewHolder: $percent")
                it.progressbar.progress = percent
            }

            if(position == userSelectedIndex) {
                it.root.setBackgroundResource(R.color.dream_purple)
            } else {
                it.root.background = null
            }
        }

        holder.itemView.setOnClickListener {
            setSelectedIndex(position)
        }
    }

    private fun setSelectedIndex(index: Int) {
        val previousIndex = userSelectedIndex
        userSelectedIndex = index

        notifyItemChanged(previousIndex)
        notifyItemChanged(userSelectedIndex)
    }
}