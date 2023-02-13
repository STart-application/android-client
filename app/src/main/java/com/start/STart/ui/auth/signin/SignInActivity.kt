package com.start.STart.ui.auth.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}