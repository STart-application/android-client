package com.start.STart.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.databinding.ActivityHomeBinding
import com.start.STart.ui.home.event.EventActivity
import com.start.STart.ui.home.festival.FestivalActivity
import com.start.STart.ui.home.info.InfoActivity
import com.start.STart.ui.home.setting.SettingActivity

class HomeActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val sliderAdapter by lazy { SliderAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = sliderAdapter.apply {
            list = listOf(
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
            )
        }

        TabLayoutMediator(binding.indicator, binding.slider) { _, _ ->
        }.attach()

        binding.btnInfo.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }

        binding.btnFestival.setOnClickListener {
            startActivity(Intent(this, FestivalActivity::class.java))
        }

        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.btnEvent.setOnClickListener {
            startActivity(Intent(this, EventActivity::class.java))
        }
    }
}