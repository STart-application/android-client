package com.start.STart.ui.home.setting.devinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.databinding.ActivityDevInfoBinding

class DevInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDevInfoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}