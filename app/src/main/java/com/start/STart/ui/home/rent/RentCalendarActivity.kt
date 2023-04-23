package com.start.STart.ui.home.rent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.databinding.ActivityRentCalendarBinding
import com.start.STart.ui.home.rent.calendar.RentViewPagerAdapter
import com.start.STart.util.DateFormatter
import com.start.STart.util.getParcelableExtra
import org.threeten.bp.LocalDate
import java.util.*

class RentCalendarActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentCalendarBinding.inflate(layoutInflater) }
    private val viewModel: RentCalendarViewModel by viewModels()

    private val rentViewPagerAdapter by lazy { RentViewPagerAdapter() }


    private lateinit var rentItem: RentItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rentItem = intent.getParcelableExtra(key=RentItem.KEY_RENT_ITEM_TYPE)!!

        binding.textItemTitle.text = rentItem.category
        binding.textPurposeValue.text = rentItem.purpose
        Glide.with(this)
            .load(getString(R.string.url_polar_bear))
            .centerCrop()
            .into(binding.imageTitle)

        initToolbar()
        initViewPager()
        initViewModelListeners()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "상시사업 예약"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewPager() {
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
    }

    private fun updateCalendar(){
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, binding.monthViewPager.currentItem - rentViewPagerAdapter.baseIndex)

        binding.textMonthTitle.text = "${calendar.get(Calendar.MONTH) + 1}월 예약 현황"
        viewModel.loadCalendar(binding.monthViewPager.currentItem, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, rentItem.name)
    }

    @Suppress("unchecked_cast")
    private fun initViewModelListeners() {

        viewModel.loadItemCountResult.observe(this) {
            if(it.isSuccessful) {
                binding.textMaxCountValue.text = "${it.data as Int}개"
            }
            //TODO: 예외 처리
        }

        viewModel.loadCalendarResult.observe(this) { pair ->
            val viewPagerIndex = pair.first
            val resultModel = pair.second

            if(resultModel.isSuccessful) {
                val rentDataMap = resultModel.data as Map<LocalDate, Int>

                if(rentDataMap.isNotEmpty()) {
                    val viewHolder  = (binding.monthViewPager.getChildAt(0) as RecyclerView?)?.findViewHolderForAdapterPosition(viewPagerIndex) as RentViewPagerAdapter.RentViewPagerViewHolder?

                    viewHolder?.calendarAdapter?.list?.let { it ->

                        it.forEachIndexed { index, rentDateItem ->
                            val dateKey = DateFormatter.format(rentDateItem.date.time)

                            val rentDataList = rentDataMap[LocalDate.parse(dateKey)]


                            if (rentDataList != null) {
                                rentDateItem.count = rentDataList
                            } else {
                                rentDateItem.count = 0
                            }
                            rentDateItem.total = (viewModel.loadItemCountResult.value?.data as Int)
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