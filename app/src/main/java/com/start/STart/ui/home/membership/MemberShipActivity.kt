package com.start.STart.ui.home.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.databinding.ActivityMemberShipBinding

class MemberShipActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMemberShipBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "자취회비 납부 확인"
    }
}