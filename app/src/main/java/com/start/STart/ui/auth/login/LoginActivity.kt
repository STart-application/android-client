package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.R
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityLoginBinding
import com.start.STart.ui.auth.register.PolicyActivity
import com.start.STart.ui.auth.reset.ResetPasswordAuthActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    private var flagRestPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewModelListeners()
    }

    private fun init(){
        flagRestPassword = intent.getBooleanExtra(Constants.FLAG_RESET_PASSWORD, false)
    }

    private fun initView() {
        binding.btnLogin.isEnabled = true // TODO: 유효성 검증 필요
        if(flagRestPassword) {
            binding.textResult.text = "비밀번호가 재설정되었습니다.\n다시 로그인 해주세요!"
        }
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
            } else {
                binding.textResult.text = it.message
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
            binding.textResult.text = resources.getString(R.string.sign_in_fail)
        }
    }

    private val isInputValid: Boolean
        get() = binding.inputStudentId.text.toString().isNotBlank() and
                binding.inputPassword.text.toString().isNotBlank()
}