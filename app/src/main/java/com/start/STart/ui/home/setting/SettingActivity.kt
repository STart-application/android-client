package com.start.STart.ui.home.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.databinding.ActivitySettingBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.textLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                TokenHelper.clearToken()
                withContext(Dispatchers.Main) {
                    startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                    finish()
                }
            }
        }
    }
}