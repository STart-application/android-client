package com.start.STart.ui.home.rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.databinding.ActivityRentCalendarBinding
import com.start.STart.ui.home.rent.calendar.RentCalendarAdapter
import com.start.STart.ui.home.rent.calendar.RentDateItem
import com.start.STart.ui.home.rent.calendar.RentViewPagerAdapter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Random

class RentCalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentCalendarBinding.inflate(layoutInflater) }

    private val rentViewPagerAdapter by lazy { RentViewPagerAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.monthViewPager.adapter = rentViewPagerAdapter
        binding.monthViewPager.offscreenPageLimit = 3
        binding.monthViewPager.setCurrentItem(rentViewPagerAdapter.baseIndex, false)
        binding.monthViewPager.setPageTransformer { page, position ->

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, binding.monthViewPager.currentItem - rentViewPagerAdapter.baseIndex)
            binding.textMonthTitle.text = "${calendar.get(Calendar.MONTH) + 1}월 예약 현황"
        }

        binding.btnPreviousMonth.setOnClickListener {
            binding.monthViewPager.currentItem += 1
        }

        binding.btnNextMonth.setOnClickListener {
            binding.monthViewPager.currentItem += 1
        }


    }
}