package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.start.STart.databinding.ActivityValidateStudentCardBinding

class StudentCardUploadActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentCardBinding.inflate(layoutInflater) }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.inputStudentCard)

            binding.btnNext.isEnabled = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.layoutInputStudentCard.setOnClickListener {
            imageLauncher.launch("image/*")
        }
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, RegisterCompleteActivity::class.java))
        }
    }
}