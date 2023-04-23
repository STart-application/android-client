package com.start.STart.ui.home.rent.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.rent.response.RentData
import com.start.STart.databinding.FragmentMonthBinding
import com.start.STart.ui.home.rent.util.getDateList
import java.util.Calendar

class RentViewPagerAdapter(val onDateSelectedListener: RentCalendarAdapter.OnDataSelectedListener):  RecyclerView.Adapter<RentViewPagerAdapter.RentViewPagerViewHolder>(){

    var currentCalendar: Calendar = Calendar.getInstance()

    val maxIndex = 1000
    val baseIndex = (maxIndex / 2)

    inner class RentViewPagerViewHolder(val binding: FragmentMonthBinding): RecyclerView.ViewHolder(binding.root) {
        val calendarAdapter = RentCalendarAdapter(onDateSelectedListener)

        init {
            initCalendarRecyclerView()
        }
        private fun initCalendarRecyclerView() {
            binding.calenderRecyclerView.adapter = calendarAdapter
            //binding.calenderRecyclerView.addItemDecoration(GridDividerItemDecoration(, ContextCompat.getColor(binding.root.context, R.color.dream_gray_light)))
        }

        fun updateCalendar(calendar: Calendar) {
            calendarAdapter.calendarMonth = calendar.get(Calendar.MONTH)
            calendarAdapter.list = getDateList(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
            binding.root.requestLayout()
        }

    }

    fun update(list: List<RentData>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewPagerAdapter.RentViewPagerViewHolder {
        val binding = FragmentMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RentViewPagerViewHolder(binding)
    }

    override fun getItemCount() = maxIndex

    override fun onBindViewHolder(holder: RentViewPagerAdapter.RentViewPagerViewHolder, position: Int) {
        val nextCalendar = Calendar.getInstance()
        nextCalendar.add(Calendar.MONTH, position - baseIndex)

        currentCalendar = nextCalendar

        holder.updateCalendar(nextCalendar)
    }

}