package com.start.STart.ui.home.festival

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalBinding

class FestivalInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFestivalBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}