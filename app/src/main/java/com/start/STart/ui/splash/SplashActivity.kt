package com.start.STart.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.api.ApiClient
import com.start.STart.api.TokenInterceptor
import com.start.STart.databinding.ActivitySplashBinding
import com.start.STart.ui.auth.login.LoginActivity
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val isSuccessful = TokenHelper.tryLoginWithAccessToken()
            if(isSuccessful) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java))
            }
        }

        binding.btnMove.setOnClickListener {
            startActivity(Intent(this, LoginOrSkipActivity::class.java))
            finish()
        }
    }
}