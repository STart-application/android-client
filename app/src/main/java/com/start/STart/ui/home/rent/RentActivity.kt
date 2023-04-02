package com.start.STart.ui.home.rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivityRentBinding

class RentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}