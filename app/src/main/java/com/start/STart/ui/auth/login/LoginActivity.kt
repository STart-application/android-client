package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityLoginBinding
import com.start.STart.ui.auth.register.PolicyActivity
import com.start.STart.ui.auth.reset.ResetPasswordActivity
import com.start.STart.ui.auth.reset.ResetPasswordAuthActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        binding.btnLogin.setOnClickListener {
            signIn()
        }
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, PolicyActivity::class.java))
        }
        binding.textResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordAuthActivity::class.java))
        }
    }

    private fun signIn() {
        if (isInputValid) {
            startActivity(Intent(this, HomeActivity::class.java).apply {
                putExtra(Constants.SIGN_IN, true)
            })
            finish()
        } else {
            showSignInFailText()
        }
    }

    private val isInputValid: Boolean
        get() = binding.inputStudentId.text.toString().isNotBlank() and
                binding.inputPassword.text.toString().isNotBlank()

    private fun showSignInFailText() {
        binding.textSignInFail.visibility = View.VISIBLE
    }

    private fun hideSignInFailText() {
        binding.textSignInFail.visibility = View.INVISIBLE
    }
}