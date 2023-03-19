package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.start.STart.R
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateSmsBinding
import com.start.STart.util.AppRegex
import com.start.STart.util.Constants
import com.start.STart.util.getParcelableExtra

class VerifyCodeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateSmsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<VerifyCodeViewModel>()

    private lateinit var registerData: RegisterData

    private var isCodeSent = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewListeners()
        initViewModelListeners()
    }

    private fun init() {
        registerData = intent.getParcelableExtra(key = Constants.KEY_REGISTER_DATA)!!
    }

    private fun initView() {
        binding.btnNext.isEnabled = true // TODO: 테스트 끝나면 삭제

        binding.inputPhone.addTextChangedListener {
            binding.btnRequest.isEnabled = Regex(AppRegex.PHONE_VALIDATE).matches(it.toString())
        }
    }

    private fun initViewListeners() {
        binding.inputCode.addTextChangedListener {
            binding.btnValidate.isEnabled = it.toString().length == 6 && isCodeSent
        }

        binding.btnRequest.setOnClickListener {
            viewModel.sendCode(Regex(AppRegex.PHONE_VALIDATE)
                .replace(binding.inputPhone.text.toString(), "01$1-$2-$3")
            )
        }

        binding.btnValidate.setOnClickListener {
            // TODO: 코드 유효성 검사 및 전송 번호 저장
            viewModel.verifyCode(Regex(AppRegex.PHONE_VALIDATE)
                .replace(binding.inputPhone.text.toString(), "01$1-$2-$3"),
            binding.inputCode.text.toString())
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, StudentCardUploadActivity::class.java).apply {
                putExtra(Constants.KEY_REGISTER_DATA, registerData.also {
                    it.phoneNo = Regex(AppRegex.PHONE_VALIDATE)
                        .replace(binding.inputPhone.text.toString(), "01$1-$2-$3")
                })
            })
        }
    }

    private fun initViewModelListeners() {
        viewModel.sendCodeResult.observe(this) { result ->
            if(result) {
                Balloon.Builder(this)
                    .setText("SMS가 전송되었습니다.")
                    .setPadding(8)
                    .setBackgroundColor(resources.getColor(R.color.dream_purple))
                    .setArrowOrientation(ArrowOrientation.START)
                    .setArrowPosition(0.5f)
                    .build()
                    .showAlignRight(binding.textCode)

                // 인증 버튼 업데이트
                isCodeSent = true
            }
        }

        viewModel.verifyCodeResult.observe(this) { result ->
            // TODO: 사용자에게 명확히 인증 성공 알림 코드 필요
            binding.btnNext.isEnabled = result.isSuccessful
            if(!result.isSuccessful) {
                Balloon.Builder(this)
                    .setText(result.message!!)
                    .setPadding(8)
                    .setBackgroundColor(resources.getColor(R.color.dream_purple))
                    .setArrowOrientation(ArrowOrientation.START)
                    .setArrowPosition(0.5f)
                    .build()
                    .showAlignRight(binding.textCode)
            }
        }
    }
}