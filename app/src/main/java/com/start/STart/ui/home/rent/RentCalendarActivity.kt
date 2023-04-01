package com.start.STart.ui.home.rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.start.STart.api.rent.response.RentData
import com.start.STart.databinding.ActivityRentCalendarBinding
import com.start.STart.ui.home.rent.calendar.RentCalendarAdapter
import com.start.STart.ui.home.rent.calendar.RentDateItem
import com.start.STart.ui.home.rent.calendar.RentViewPagerAdapter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters
import java.text.SimpleDateFormat
import java.util.*

class RentCalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentCalendarBinding.inflate(layoutInflater) }
    private val viewModel: RentCalendarViewModel by viewModels()

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


        binding.monthViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, binding.monthViewPager.currentItem - rentViewPagerAdapter.baseIndex)
                viewModel.loadCalendar(position, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, "CANOPY")
            }
        })

        binding.btnPreviousMonth.setOnClickListener {
            binding.monthViewPager.currentItem += 1
        }

        binding.btnNextMonth.setOnClickListener {
            binding.monthViewPager.currentItem += 1
        }

        initViewModelListeners()
    }

    @Suppress("unchecked_cast")
    private fun initViewModelListeners() {
        viewModel.loadCalendarResult.observe(this) {
            val viewPagerIndex = it.first
            val resultModel = it.second

            if(resultModel.isSuccessful) {
                val list = resultModel.data as List<RentData>

                if(list.isNotEmpty()) {
                    val viewHolder  = (binding.monthViewPager.getChildAt(0) as RecyclerView?)?.findViewHolderForAdapterPosition(viewPagerIndex) as RentViewPagerAdapter.RentViewPagerViewHolder?

                    viewHolder?.calendarAdapter?.list?.let { it ->
                        it.forEach { rentDateItem ->
                            rentDateItem.count = list.filter {  rentData ->
                                Log.d(null, "initViewModelListeners: ${rentData.startTime}, ${rentData.endTime}, ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(rentDateItem.date.time)} ${isDateBetween(rentData.startTime, rentData.endTime, rentDateItem.date)}")
                                isDateBetween(rentData.startTime, rentData.endTime, rentDateItem.date)
                            }.sumOf { rentData ->
                                rentData.account
                            }
                            rentDateItem.total = 10
                        }
                    }
                    viewHolder?.calendarAdapter?.notifyDataSetChanged()
                }
                Log.d(null, "initViewModelListeners: $list")
            } else {
                Log.d(null, "initViewModelListeners: false")
            }
        }
    }

    fun isDateBetween(startDate: String, endDate: String, targetDate: Calendar): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = format.parse(startDate)
        val end = format.parse(endDate)
        val target = format.parse(format.format(targetDate.time))

        return target in start..end
    }
}