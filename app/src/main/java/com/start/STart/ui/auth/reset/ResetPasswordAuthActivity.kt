package com.start.STart.ui.auth.reset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivityResetAuthBinding
import com.start.STart.databinding.ActivityResetPasswordBinding

class ResetPasswordAuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResetAuthBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
        // TODO: 비밀번호 재설정 인증 Api 로직 작성
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }
    }
}