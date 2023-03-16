package com.start.STart.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.api.ApiClient
import com.start.STart.databinding.ActivitySplashBinding
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
        
        // 로그인 시도
        tryLogin()

        // TODO: 로그인 로직 정상 동작 확인 후 버튼 삭제
        binding.btnMove.setOnClickListener {
            startActivity(Intent(this, LoginOrSkipActivity::class.java))
            finish()
        }
    }
    
    private fun tryLogin() = lifecycleScope.launch(Dispatchers.IO) {
        val isSuccessful = TokenHelper.tryLoginWithAccessToken()
        if(isSuccessful) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        } else {
            ApiClient.disableToken()
            startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java))
            finish()
        }
    }
}