package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityLoginBinding
import com.start.STart.ui.auth.register.PolicyActivity
import com.start.STart.ui.auth.reset.ResetPasswordAuthActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewModelListeners()
    }

    private fun initView() {
        binding.btnLogin.isEnabled = true // TODO: 유효성 검증 필요
        initToolbar()
        initViewListeners()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "로그인"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {
        binding.btnLogin.setOnClickListener {
            login(binding.inputStudentId.text.toString(), binding.inputPassword.text.toString())
        }
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, PolicyActivity::class.java))
        }
        binding.textResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordAuthActivity::class.java))
        }
    }

    private fun initViewModelListeners() {
        viewModel.loginResult.observe(this) {
            if(it.isSuccessful) {
                viewModel.loadMember()
            }
        }
        viewModel.loadMemberResult.observe(this) {
            if(it.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
    }

    private fun login(studentId: String, password: String) {
        if (isInputValid) {
            viewModel.login(studentId, password)
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