package com.start.STart.ui.auth.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}