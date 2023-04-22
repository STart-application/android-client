package com.start.STart.ui.home.festival.info

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalInfoBinding
import com.start.STart.util.dp2px

class FestivalInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFestivalInfoBinding.inflate(layoutInflater) }
    private val viewModel: FestivalInfoViewModel by viewModels()
    private val festivalInfoAdapter by lazy { FestivalInfoAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initViewPager()

        viewModel.loadFestivalInfo()
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "축제 정보"
    }

    private fun initViewPager() {
        val tabLayoutList = listOf(binding.tabLayout1, binding.tabLayout2)
        val tabTextList = listOf(binding.textTab1, binding.textTab2)
        val tabViewList = listOf(binding.tab1, binding.tab2)

        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.adapter = festivalInfoAdapter
        updateTab(binding.viewPager.currentItem, tabTextList, tabViewList)

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_IDLE) {
                    updateTab(binding.viewPager.currentItem, tabTextList, tabViewList)
                }
            }
        })

        tabLayoutList.forEachIndexed { index, view ->
            view.setOnClickListener {
                binding.viewPager.setCurrentItem(index, true)
            }
        }
    }

    fun updateTab(enabledIndex: Int, textList: List<TextView>, viewList: List<View>) {

        textList.zip(viewList).forEachIndexed { index, pair ->
            if(index == enabledIndex) {
                pair.second.let {
                    it.layoutParams = it.layoutParams.apply { height = dp2px(5f).toInt() }
                }
                pair.first.setTextColor(ContextCompat.getColor(this, R.color.dream_purple))
                pair.second.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple))
            } else {
                pair.second.let {
                    it.layoutParams = it.layoutParams.apply { height = dp2px(3f).toInt() }
                }
                pair.first.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
                pair.second.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
            }

        }
    }
}