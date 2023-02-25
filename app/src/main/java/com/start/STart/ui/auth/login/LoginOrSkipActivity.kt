package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityLoginOrSkipBinding

class LoginOrSkipActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginOrSkipBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnNoSignIn.setOnClickListener {
            showNoSignInDialog()
        }
    }

    private fun showNoSignInDialog() {
        val dialogFragment = ConfirmNoSignInDialog()
        dialogFragment.show(supportFragmentManager, "ConfirmNoSignInDialog")
    }
}