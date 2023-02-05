package com.start.STart.ui.home.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInfoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}