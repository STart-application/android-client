package com.start.STart.ui.home.rent.util

import com.start.STart.ui.home.rent.calendar.RentDateItem
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.Calendar

fun getDateList(year: Int, month: Int, day: Int = 1): List<RentDateItem> {
    val realStartDate = LocalDate.of(year, month, day)

    val startDate = realStartDate
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    val endDate = startDate.plusWeeks(5)
        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))

    val dateList = mutableListOf<RentDateItem>()
    var currentDate = startDate

    while (!currentDate.isAfter(endDate)) {
        val calendar = Calendar.getInstance()
        calendar.set(currentDate.year, currentDate.monthValue - 1, currentDate.dayOfMonth)
        dateList.add(RentDateItem(calendar, 0, 0))
        currentDate = currentDate.plusDays(1)
    }

    return dateList
}