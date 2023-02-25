package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityValidateStudentCardBinding

class ValidateStudentCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentCardBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, RegisterCompleteActivity::class.java))
        }
    }
}