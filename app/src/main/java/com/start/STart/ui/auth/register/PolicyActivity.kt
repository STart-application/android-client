package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ValidateStudentInfoActivity::class.java))
        }
    }
}