package com.start.STart.ui.auth.reset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.start.STart.R
import com.start.STart.databinding.ActivityResetAuthBinding
import com.start.STart.util.Constants
import com.start.STart.util.TIMER_ENDED
import com.start.STart.util.formatTimerMilliseconds
import com.start.STart.util.showErrorToast
import com.start.STart.util.showSuccessToast

class ResetPasswordAuthActivity : AppCompatActivity() {
    private val binding by lazy { ActivityResetAuthBinding.inflate(layoutInflater) }
    private val viewModel: ResetPasswordAuthViewModel by viewModels()

    // 학번 저장
    private lateinit var sentStudentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initViewListeners()
        initLiveDataObservers()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "비밀번호 재설정"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {
        // 학번 입력 검증
        binding.inputStudentId.addTextChangedListener {
            binding.btnAuthRequest.isEnabled = it.toString().length == 8
        }

        // 인증 요청
        binding.btnAuthRequest.setOnClickListener {
            sentStudentId = binding.inputStudentId.text.toString()
            viewModel.sendAuthCodeForResetPassword(sentStudentId)
        }

        // 코드 입력 검증
        binding.inputCode.addTextChangedListener {
            updateValidateBtn()
        }

        // 코드 검증 요청
        binding.btnValidate.setOnClickListener {
            viewModel.verifyAuthCode(sentStudentId, binding.inputCode.text.toString())
        }

        // 자동 이동으로 인해 호출될 일 없음
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                putExtra(Constants.KEY_STUDENT_ID, sentStudentId)
            })
        }

    }

    private fun updateValidateBtn() {
        binding.btnValidate.isEnabled =
            binding.inputCode.text.toString().length == 6 &&
            viewModel.sendAuthCodeResult.value?.isSuccessful?:false &&
            viewModel.timerLiveData.value != TIMER_ENDED
    }

    private fun initLiveDataObservers() {
        // 인증 요청 결과
        viewModel.sendAuthCodeResult.observe(this) {
            if(it.isSuccessful) {
                binding.btnAuthRequest.text = "재전송"
                viewModel.restartTimer()
                showSuccessToast(this, getString(R.string.auth_sms_success))
            } else {
                viewModel.stopTimer()
                showErrorToast(this, it.message!!)
            }
        }

        // 타이머 업데이트
        viewModel.timerLiveData.observe(this) {
            binding.textTimer.text = formatTimerMilliseconds(it)
            if(it == TIMER_ENDED) {
                updateValidateBtn()
            }
        }

        // 코드 검증 결과
        viewModel.verifyAuthCodeResult.observe(this) {
            if(it.isSuccessful) {
                binding.btnNext.isEnabled = true
                // 인증 성공 시 자동으로 이동
                startActivity(Intent(this, ResetPasswordActivity::class.java).apply {
                    putExtra(Constants.KEY_STUDENT_ID, sentStudentId)
                })
            } else {
                showErrorToast(this, it.message!!)
            }
        }
    }
}