package com.start.STart.ui.auth.reset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.databinding.ActivityResetPasswordBinding
import com.start.STart.ui.auth.login.LoginActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.*

class ResetPasswordActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResetPasswordBinding.inflate(layoutInflater) }
    private val viewModel: ResetPasswordViewModel by viewModels()

    var studentId: String? = null

    private var isPasswordValid = false
    private var isPasswordConfirmValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
    }

    private fun init(){
        studentId = intent.getStringExtra(Constants.KEY_STUDENT_ID)
    }

    private fun initView() {
        initToolbar()

        binding.inputPassword.addTextChangedListener {
            checkPassword()
        }

        binding.inputPasswordConfirm.addTextChangedListener {
            checkPasswordConfirm()
        }

        binding.btnConfirm.setOnClickListener {
            viewModel.resetPassword(studentId!!, binding.inputPassword.text.toString())
        }

        viewModel.resetPasswordResult.observe(this) {
            if(it.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(Constants.FLAG_RESET_PASSWORD, true)
                })
            } else {
                binding.btnConfirm.showTopBalloon(it.message)
            }
        }
    }

    fun initToolbar() {
        binding.toolbar.textTitle.text = "비밀번호 재설정"
        binding.toolbar.btnBack.setOnClickListener { finish() }
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
        checkPasswordConfirm()
    }

    private fun checkPasswordConfirm() {
        val passwordConfirm = binding.inputPasswordConfirm.text.toString()
        isPasswordConfirmValid = false
        if (passwordConfirm.isEmpty()) {
            binding.textPasswordConfirmCaption.text = ""
        } else {
            if (binding.inputPassword.text.toString() == passwordConfirm) {
                binding.textPasswordConfirmCaption.setSuccessText(
                    resources.getString(R.string.password_equal_success)
                )
                isPasswordConfirmValid = true
            } else {
                binding.textPasswordConfirmCaption.setFailText(
                    resources.getString(R.string.password_equal_fail)
                )
            }
        }
        updateButtonEnabled()
    }

    private fun updateButtonEnabled() {
        binding.btnConfirm.isEnabled = isPasswordValid && isPasswordConfirmValid
    }
}