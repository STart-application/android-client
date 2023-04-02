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
import com.start.STart.util.DateFormatter
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
        updateCalendar()

        binding.monthViewPager.setPageTransformer { page, position ->

        }


        binding.monthViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_IDLE) {
                    updateCalendar()
                }
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

    private fun updateCalendar(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, binding.monthViewPager.currentItem - rentViewPagerAdapter.baseIndex)

        binding.textMonthTitle.text = "${calendar.get(Calendar.MONTH) + 1}월 예약 현황"
        viewModel.loadCalendar(binding.monthViewPager.currentItem, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, "CANOPY")
    }

    @Suppress("unchecked_cast")
    private fun initViewModelListeners() {
        viewModel.loadCalendarResult.observe(this) { pair ->
            val viewPagerIndex = pair.first
            val resultModel = pair.second

            if(resultModel.isSuccessful) {
                val list = resultModel.data as List<RentData>

                if(list.isNotEmpty()) {
                    val viewHolder  = (binding.monthViewPager.getChildAt(0) as RecyclerView?)?.findViewHolderForAdapterPosition(viewPagerIndex) as RentViewPagerAdapter.RentViewPagerViewHolder?

                    val sortedList = list.sortedBy { it.startTime }

                    val rentDataByDate = sortedList.groupBy { rentData ->
                        rentData.startTime
                    }

                    viewHolder?.calendarAdapter?.list?.let { it ->
                        it.forEachIndexed { index, rentDateItem ->
                            val dateKey = DateFormatter.format(rentDateItem.date.time)
                            val rentDataList = rentDataByDate[dateKey]
                            if (rentDataList != null) {
                                rentDateItem.count = rentDataList.sumOf { rentData -> rentData.account }
                            } else {
                                rentDateItem.count = 0
                            }
                            rentDateItem.total = 10
                            viewHolder.calendarAdapter.notifyItemChanged(index)
                        }
                    }

                }
                Log.d(null, "initViewModelListeners: $list")
            } else {
                Log.d(null, "initViewModelListeners: false")
            }
        }
    }
}