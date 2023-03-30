package com.start.STart.ui.home.rent.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.databinding.FragmentMonthBinding
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

class RentViewPagerAdapter:  RecyclerView.Adapter<RentViewPagerAdapter.RentViewPagerViewHolder>(){
    // 이 친구는 고정된 값
    val currentCalendar = Calendar.getInstance()

    val maxIndex = 1000
    val baseIndex = (maxIndex / 2)

    inner class RentViewPagerViewHolder(val binding: FragmentMonthBinding): RecyclerView.ViewHolder(binding.root) {
        val calendarAdapter = RentCalendarAdapter()

        init {
            initCalendarRecyclerView()
        }
        private fun initCalendarRecyclerView() {
            binding.calenderRecyclerView.adapter = calendarAdapter
        }

        fun updateCalendar(calendar: Calendar) {
            Log.d(null, "setCalendar: ${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")
            binding.textMonth.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
            calendarAdapter.list = getDateList(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)
        }


        private fun getDateList(year: Int, month: Int, day: Int = 1): List<RentDateItem> {
            val realStartDate = LocalDate.of(year, month, day)
            val startDate = realStartDate
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            val endDate = realStartDate.with(TemporalAdjusters.lastDayOfMonth())
            val dateList = mutableListOf<RentDateItem>()
            var currentDate = startDate

            val random = Random()
            while (!currentDate.isAfter(endDate)) {
                val calendar = Calendar.getInstance()
                calendar.set(currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth)
                dateList.add(RentDateItem(calendar, random.nextInt(70), 100))
                currentDate = currentDate.plusDays(1)
            }

            return dateList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentViewPagerAdapter.RentViewPagerViewHolder {
        val binding = FragmentMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RentViewPagerViewHolder(binding)
    }

    override fun getItemCount() = maxIndex

    override fun onBindViewHolder(holder: RentViewPagerAdapter.RentViewPagerViewHolder, position: Int) {
        val nextCalendar = currentCalendar.clone() as Calendar
        nextCalendar.add(Calendar.MONTH, position - baseIndex)
        holder.updateCalendar(nextCalendar)
    }

}