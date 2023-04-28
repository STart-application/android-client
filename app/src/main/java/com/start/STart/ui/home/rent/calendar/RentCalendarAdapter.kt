package com.start.STart.ui.home.rent.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.databinding.ItemRentCalendarBinding
import java.util.Calendar

class RentCalendarAdapter(val onDateSelectedListener: OnDateSelectedListener): RecyclerView.Adapter<RentCalendarAdapter.RentCalendarViewHolder>() {

    var calendarMonth: Int = 0

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

        val context = holder.binding.root.context

        holder.binding.let {
            // 날짜
            val date = item.date.get(Calendar.DATE)
            val dayOfWeek = item.date.get(Calendar.DAY_OF_WEEK)
            val today = Calendar.getInstance()
            val textColor = when {
                // 오늘
                item.date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    item.date.get(Calendar.DATE) == today.get(Calendar.DATE) -> R.color.dream_purple

                item.date.get(Calendar.MONTH) != calendarMonth -> R.color.text_caption
                dayOfWeek == Calendar.SATURDAY -> R.color.dream_blue
                dayOfWeek == Calendar.SUNDAY -> R.color.dream_red
                else -> R.color.black
            }
            it.textDate.text = if (item.date.get(Calendar.MONTH) != calendarMonth) "0" else date.toString()
            it.textDate.setTextColor(ContextCompat.getColor(context, textColor))

            // 프로그래스바
            if(item.total == 0 || item.count == 0) {
                it.progressbar.visibility = View.INVISIBLE
            } else {
                it.progressbar.visibility = View.VISIBLE

                val percent = ((item.count.toFloat() / item.total) * 100).toInt()
                it.progressbar.progress = percent
            }

            // 선택된 날짜
            if(position == userSelectedIndex) {
                it.root.setBackgroundResource(R.drawable.rent_calendar_data_selected)
            } else {
                it.root.background = null
            }
        }

        holder.itemView.setOnClickListener {
            if(item.date.get(Calendar.MONTH) == calendarMonth) {
                setSelectedIndex(position)
                onDateSelectedListener.onClick(item)
            }
        }
    }

   fun setSelectedIndex(index: Int) {
        val previousIndex = userSelectedIndex
        userSelectedIndex = index

        notifyItemChanged(previousIndex)
        notifyItemChanged(userSelectedIndex)
    }

    fun updateSelectedIndex() {
        onDateSelectedListener.onClick(list[userSelectedIndex])
    }

    interface OnDateSelectedListener {
        fun onClick(rentDateItem: RentDateItem)
    }
}