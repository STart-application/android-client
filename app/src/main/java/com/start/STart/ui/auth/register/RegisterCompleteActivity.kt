package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityRegisterCompleteBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity

class RegisterCompleteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterCompleteBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnConfirm.setOnClickListener {
            startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }

        onBackPressedDispatcher.addCallback {
            startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}