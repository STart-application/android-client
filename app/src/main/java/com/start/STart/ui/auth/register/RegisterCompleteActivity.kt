package com.start.STart.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityRegisterCompleteBinding

class RegisterCompleteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterCompleteBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}