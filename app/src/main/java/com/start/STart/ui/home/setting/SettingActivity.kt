package com.start.STart.ui.home.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivitySettingBinding
import com.start.STart.ui.home.setting.updatehistory.UpdateHistoryActivity

class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.textUpdateLog.setOnClickListener {
            startActivity(Intent(this, UpdateHistoryActivity::class.java))
        }

        binding.icBack.setOnClickListener {
            finish()
        }
    }
}