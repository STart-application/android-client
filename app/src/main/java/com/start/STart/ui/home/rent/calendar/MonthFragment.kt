package com.start.STart.ui.home.rent.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.start.STart.databinding.FragmentMonthBinding
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.*

class MonthFragment : Fragment() {
    private var _binding: FragmentMonthBinding? = null
    val binding get() = _binding!!

    private val calendarAdapter by lazy { RentCalendarAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendarRecyclerView()
    }

    private fun initCalendarRecyclerView() {
        binding.calenderRecyclerView.adapter = calendarAdapter.apply {
            list = getDateList(2023, 3)
        }
    }

    fun setCalendar(calendar: Calendar) {
        calendarAdapter.list = getDateList(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
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
            dateList.add(RentDateItem(calendar, random.nextInt(101), random.nextInt(101)))
            currentDate = currentDate.plusDays(1)
        }

        return dateList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}