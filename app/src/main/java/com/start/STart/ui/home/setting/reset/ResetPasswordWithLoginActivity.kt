package com.start.STart.ui.home.setting.reset

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.api.member.request.ResetPasswordWithLoginBody
import com.start.STart.databinding.ActivityResetPasswordWithLoginBinding
import com.start.STart.ui.auth.login.LoginActivity
import com.start.STart.ui.auth.util.setFailText
import com.start.STart.ui.auth.util.setSuccessText
import com.start.STart.util.AppRegex
import com.start.STart.util.Constants
import com.start.STart.util.showErrorToast

class ResetPasswordWithLoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResetPasswordWithLoginBinding.inflate(layoutInflater) }
    private val viewModel: ResetPasswordWithLoginViewModel by viewModels()

    private var isPasswordValid = false
    private var isPasswordConfirmValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initRestPasswordLogic()
    }

    fun initToolbar() {
        binding.toolbar.textTitle.text = "비밀번호 재설정"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initRestPasswordLogic() {
        // 비밀번호 입력 검증
        binding.inputPassword.addTextChangedListener {
            checkPassword()
        }

        // 비밀번호 확인 입력 검증
        binding.inputPasswordConfirm.addTextChangedListener {
            checkPasswordConfirm()
        }

        // 확인 버튼 클릭
        binding.btnConfirm.setOnClickListener {
            viewModel.resetPassword(
                ResetPasswordWithLoginBody(
                    currentPassword = binding.inputOriginalPassword.text.toString(),
                    newPassword = binding.inputPasswordConfirm.text.toString()
                )
            )
        }

        viewModel.resetPasswordResult.observe(this) {
            if(it.isSuccessful) {
                // 로그인 선택 화면으로 이동
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(Constants.FLAG_RESET_PASSWORD, true)
                })
            } else {
                showErrorToast(this, it.message!!)
            }
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
        binding.btnConfirm.isEnabled = isPasswordValid && isPasswordConfirmValid && binding.inputOriginalPassword.text.toString().isNotBlank()
    }
}