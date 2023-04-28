package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.databinding.ActivityLoginBinding
import com.start.STart.ui.auth.register.PolicyActivity
import com.start.STart.ui.auth.reset.ResetPasswordAuthActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.AppRegex
import com.start.STart.util.Constants
import com.start.STart.util.setFailText
import com.start.STart.util.setSuccessText
import com.start.STart.util.showErrorToast

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()

    private var flagRestPassword: Boolean = false

    private var isPasswordValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        updateFlags()

        initViewListeners()
        initInputListeners()
        initViewModelListeners()
    }

    private fun updateFlags() {
        flagRestPassword = intent.getBooleanExtra(Constants.FLAG_RESET_PASSWORD, false)
        if (flagRestPassword) {
            binding.textResult.text = "비밀번호가 재설정되었습니다.\n다시 로그인 해주세요!"
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "로그인"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.inputStudentId.text.toString(), binding.inputPassword.text.toString())
        }
        binding.textSignUp.setOnClickListener {
            startActivity(Intent(this, PolicyActivity::class.java))
        }
        binding.textResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordAuthActivity::class.java))
        }
    }

    private fun initInputListeners() {
        binding.inputStudentId.addTextChangedListener {
            checkPassword()
            updateButton()
        }
        binding.inputPassword.addTextChangedListener {
            checkPassword()
            updateButton()
        }
    }

    private fun checkPassword() {
        val password = binding.inputPassword.text.toString()
        isPasswordValid = false
        if (password.isEmpty()) {
            binding.textPasswordCaption.text = ""
        } else {
            if (Regex(AppRegex.PASSWORD_VALIDATE).matches(password)) {
                binding.textPasswordCaption.setSuccessText(
                    resources.getString(R.string.password_format_success)
                )
                isPasswordValid = true
            } else {
                binding.textPasswordCaption.setFailText(
                    resources.getString(R.string.password_format_fail)
                )
            }
        }
    }
    private fun updateButton() {
        binding.btnLogin.isEnabled = binding.inputStudentId.text.toString().isNotBlank() && isPasswordValid
    }

    private fun initViewModelListeners() {
        viewModel.loginResult.observe(this) {
            if(it.isSuccessful) {
                viewModel.loadMember()
            } else {
                showErrorToast(this, it.message)
            }
        }
        viewModel.loadMemberResult.observe(this) {
            if(it.isSuccessful) {
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else {
                showErrorToast(this, it.message)
            }
        }
    }
}