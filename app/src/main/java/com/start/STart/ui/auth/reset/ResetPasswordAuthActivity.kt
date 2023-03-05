package com.start.STart.ui.auth.reset

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
    }
}