package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityLoginOrSkipBinding
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager

class LoginOrSkipActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginOrSkipBinding.inflate(layoutInflater) }

    private val confirmNoSignInDialog by lazy { ConfirmNoSignInDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initButton()
    }

    private fun initButton() {
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnNoSignIn.setOnClickListener {
            if(!confirmNoSignInDialog.isAdded) {
                confirmNoSignInDialog.setData(onConfirm = {
                    PreferenceManager.putBoolean(Constants.PREF_FLAG_WITHOUT_LOGIN, true)
                    startActivity(Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                })
                confirmNoSignInDialog.show(supportFragmentManager, null)
            }
        }
    }
}