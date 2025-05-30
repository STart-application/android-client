package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidatePasswordBinding
import com.start.STart.util.*

class PasswordInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidatePasswordBinding.inflate(layoutInflater) }

    private lateinit var registerData: RegisterData

    private var isPasswordValid = false
    private var isPasswordConfirmValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewListeners()
    }

    private fun init() {
        registerData = intent.getParcelableExtra(key = Constants.KEY_REGISTER_DATA)!!
    }

    private fun initView() {
        initToolbar()
        binding.inputPassword.addTextChangedListener {
            checkPassword()
        }


        binding.inputConfirmPassword.addTextChangedListener {
            checkPasswordConfirm()
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "비밀번호"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, VerifyCodeActivity::class.java).apply {
                putExtra(Constants.KEY_REGISTER_DATA, registerData.also {
                    it.appPassword = binding.inputPassword.text.toString()
                })
            })
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
        val passwordConfirm = binding.inputConfirmPassword.text.toString()
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
        binding.btnNext.isEnabled = isPasswordValid && isPasswordConfirmValid
    }
}