package com.start.STart.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivitySplashBinding
import com.start.STart.ui.auth.login.LoginActivity
import com.start.STart.ui.auth.login.LoginOrSkipActivity

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnMove.setOnClickListener {
            startActivity(Intent(this, LoginOrSkipActivity::class.java))
            finish()
        }
    }
}