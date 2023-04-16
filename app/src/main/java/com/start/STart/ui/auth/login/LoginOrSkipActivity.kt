package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.ActivityLoginOrSkipBinding

class LoginOrSkipActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginOrSkipBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.composeView.setContent {
            Cloudy(radius = 10){

            }
        }

        initViewListeners()
    }
    fun hideCompose() {
        binding.composeView.visibility = View.GONE
    }

    private fun initViewListeners() {
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnNoSignIn.setOnClickListener {
            binding.composeView.visibility = View.VISIBLE
            showNoSignInDialog()
        }
    }

    private fun showNoSignInDialog() {
        val dialogFragment = ConfirmNoSignInDialog()
        dialogFragment.show(supportFragmentManager, "ConfirmNoSignInDialog")
    }
}