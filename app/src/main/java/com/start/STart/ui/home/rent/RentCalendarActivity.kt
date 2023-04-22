package com.start.STart.ui.home.rent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.start.STart.api.rent.response.RentData
import com.start.STart.databinding.ActivityRentCalendarBinding
import com.start.STart.ui.home.rent.calendar.RentViewPagerAdapter
import com.start.STart.util.DateFormatter
import java.util.*

class RentCalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentCalendarBinding.inflate(layoutInflater) }
    private val viewModel: RentCalendarViewModel by viewModels()

    private val rentViewPagerAdapter by lazy { RentViewPagerAdapter() }


    private lateinit var type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        type = intent.getStringExtra(RentItem.KEY_RENT_ITEM_TYPE)!!

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
            binding.monthViewPager.currentItem -= 1
        }

        binding.btnNextMonth.setOnClickListener {
            binding.monthViewPager.currentItem += 1
        }

        binding.btnRent.setOnClickListener {
            startActivity(Intent(this, RentActivity::class.java))
        }

        initViewModelListeners()
    }

    private fun updateCalendar(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, binding.monthViewPager.currentItem - rentViewPagerAdapter.baseIndex)

        binding.textMonthTitle.text = "${calendar.get(Calendar.MONTH) + 1}월 예약 현황"
        viewModel.loadCalendar(binding.monthViewPager.currentItem, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, type)
    }

    @Suppress("unchecked_cast")
    private fun initViewModelListeners() {
        viewModel.loadCalendarResult.observe(this) { pair ->
            val viewPagerIndex = pair.first
            val resultModel = pair.second

            if(resultModel.isSuccessful) {
                val rentDataMap = resultModel.data as Map<String, List<RentData>>?

                if(rentDataMap?.isNotEmpty() == true) {
                    val viewHolder  = (binding.monthViewPager.getChildAt(0) as RecyclerView?)?.findViewHolderForAdapterPosition(viewPagerIndex) as RentViewPagerAdapter.RentViewPagerViewHolder?

                    viewHolder?.calendarAdapter?.list?.let { it ->
                        it.forEachIndexed { index, rentDateItem ->
                            val dateKey = DateFormatter.format(rentDateItem.date.time)

                            val rentDataList = rentDataMap[dateKey]


                            if (rentDataList != null) {
                                //rentDateItem.count = rentDataList.first
                            } else {
                                rentDateItem.count = 0
                            }
                            //rentDateItem.total = rentDataList?.second ?: 0
                            viewHolder.calendarAdapter.notifyItemChanged(index)
                        }
                    }

                } else {

                }
                Log.d(null, "initViewModelListeners: $rentDataMap")
            } else {
                Log.d(null, "initViewModelListeners: false")
            }
        }
    }
}