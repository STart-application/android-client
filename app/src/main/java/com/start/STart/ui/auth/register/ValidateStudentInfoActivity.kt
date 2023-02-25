package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityValidateStudentInfoBinding

class ValidateStudentInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ValidatePasswordActivity::class.java))
        }
    }
}