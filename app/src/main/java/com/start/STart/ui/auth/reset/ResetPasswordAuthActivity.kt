package com.start.STart.ui.auth.reset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.databinding.ActivityResetAuthBinding
import com.start.STart.util.Constants
import com.start.STart.util.showRightBalloon

class ResetPasswordAuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResetAuthBinding.inflate(layoutInflater) }
    private val viewModel: ResetPasswordAuthViewModel by viewModels()

    private lateinit var sentStudentId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
        initViewModelListeners()
    }

    private fun initViewListeners() {
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                putExtra(Constants.KEY_STUDENT_ID, sentStudentId)
            })
        }
        binding.inputStudentId.addTextChangedListener {
            binding.btnAuthRequest.isEnabled = it.toString().length == 8
        }
        binding.btnAuthRequest.setOnClickListener {
            sentStudentId = binding.inputStudentId.text.toString()
            viewModel.sendAuthCodeForResetPassword(sentStudentId)
        }

        binding.btnAuthConfirm.setOnClickListener {
            // TODO: 인증 코드 6자리 검증 절차 추가
            viewModel.verifyAuthCode(sentStudentId, binding.inputAuthCode.text.toString())
        }
    }

    private fun initViewModelListeners() {
        viewModel.sendAuthCodeResult.observe(this) {
            if(it.isSuccessful) {
                binding.btnAuthConfirm.isEnabled = true
                binding.textAuthCode.showRightBalloon(resources.getString(R.string.auth_sms_success))
            } else {
                binding.textStudentId.showRightBalloon(it.message)
            }
        }

        viewModel.verifyAuthCodeResult.observe(this) {
            if(it.isSuccessful) {
                binding.btnNext.isEnabled = true
                // 인증 성공 시 자동으로 이동
                startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                    putExtra(Constants.KEY_STUDENT_ID, sentStudentId)
                })
            } else {
                binding.textAuthCode.showRightBalloon(it.message)
            }
        }
    }
}