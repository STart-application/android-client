package com.start.STart.ui.home.rent

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.databinding.ActivityRentCalendarBinding
import com.start.STart.ui.home.rent.calendar.RentCalendarAdapter
import com.start.STart.ui.home.rent.calendar.RentDateItem
import com.start.STart.ui.home.rent.calendar.RentViewPagerAdapter
import com.start.STart.util.DateFormatter
import com.start.STart.util.getMember
import com.start.STart.util.getParcelableExtra
import es.dmoral.toasty.Toasty
import org.threeten.bp.LocalDate
import java.util.*

class RentCalendarActivity : AppCompatActivity(), RentCalendarAdapter.OnDataSelectedListener {
    private val binding by lazy { ActivityRentCalendarBinding.inflate(layoutInflater) }
    private val viewModel: RentCalendarViewModel by viewModels()

    private val rentViewPagerAdapter by lazy { RentViewPagerAdapter(this) }


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
            if(getMember() != null) {
                startActivity(Intent(this, RentActivity::class.java).apply {
                    putExtra(RentItem.KEY_RENT_ITEM_TYPE, rentItem as Parcelable)
                })
            } else {
                Toasty.info(this, "로그인이 필요한 기능입니다.").show()
            }
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
            } else {
                binding.textMaxCountValue.text = "0개"
            }
        }

        viewModel.loadCalendarResult.observe(this) { pair ->
            val viewPagerIndex = pair.first
            val resultModel = pair.second

            if(resultModel.isSuccessful) {
                val rentDataMap = resultModel.data as Map<LocalDate, Int>

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
                Log.d(null, "initViewModelListeners: $rentDataMap")
            } else {
                Log.d(null, "initViewModelListeners: false")
            }
        }
    }

    override fun onClick(rentDateItem: RentDateItem) {
        binding.textSelectedDate.text = "선택한 날짜: ${rentDateItem.date.get(Calendar.DATE)}일"
        binding.textValidCount.text = "${rentDateItem.total - rentDateItem.count}개 대여 가능"
    }
}