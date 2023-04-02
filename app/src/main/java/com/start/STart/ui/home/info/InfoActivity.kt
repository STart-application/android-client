package com.start.STart.ui.home.info

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.R
import com.start.STart.databinding.ActivityInfoBinding
import com.start.STart.util.dp2px

class InfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInfoBinding.inflate(layoutInflater) }
    private val infoAdapter by lazy { InfoAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tabLayoutList = listOf(binding.tabLayout1, binding.tabLayout2, binding.tabLayout3)
        val tabTextList = listOf(binding.textTab1, binding.textTab2, binding.textTab3)
        val tabViewList = listOf(binding.tab1, binding.tab2, binding.tab3)

        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.viewPager.adapter = infoAdapter
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
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "총학생회 설명"
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
                pair.first.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_background))
                pair.second.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_background))
            }

        }
    }
}