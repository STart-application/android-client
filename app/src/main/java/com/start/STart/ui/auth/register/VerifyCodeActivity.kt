package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateSmsBinding
import com.start.STart.util.AppRegex
import com.start.STart.util.Constants
import com.start.STart.util.formatTimerMilliseconds
import com.start.STart.util.getParcelableExtra
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class VerifyCodeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateSmsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<VerifyCodeViewModel>()

    private lateinit var registerData: RegisterData

    private val validateTimeMillis = 3000 * 60
    private var timerJob: Job? = null
    private var startTime = 0L
    private var isCodeSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerData = intent.getParcelableExtra(key = Constants.KEY_REGISTER_DATA)!!

        initToolbar()
        initViewListeners()
        initViewModelListeners()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "휴대폰 인증"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {
        binding.inputPhone.addTextChangedListener {
            binding.btnRequest.isEnabled = Regex(AppRegex.PHONE_VALIDATE).matches(it.toString())
        }

        binding.inputCode.addTextChangedListener {
            updateValidateButton()
        }

        binding.btnRequest.setOnClickListener {
            val phone = Regex(AppRegex.PHONE_VALIDATE).replace(binding.inputPhone.text.toString(), "01$1-$2-$3")
            viewModel.sendCode(phone)

        }

        binding.btnValidate.setOnClickListener {
            val phone = Regex(AppRegex.PHONE_VALIDATE).replace(binding.inputPhone.text.toString(), "01$1-$2-$3")
            val code = binding.inputCode.text.toString()

            viewModel.verifyCode(phone, code)
        }

        binding.btnNext.setOnClickListener {
            moveNext()
        }
    }

    private fun moveNext() {
        startActivity(Intent(this, StudentCardUploadActivity::class.java).apply {
            putExtra(Constants.KEY_REGISTER_DATA, registerData.also {
                it.phoneNo = Regex(AppRegex.PHONE_VALIDATE)
                    .replace(binding.inputPhone.text.toString(), "01$1-$2-$3")
            })
        })
    }

    private fun initViewModelListeners() {
        viewModel.sendCodeResult.observe(this) { result ->
            if(result.isSuccessful) {
                Toasty.success(this, "SMS가 전송되었습니다.").show()
                startTimer()
            } else {
                result.message?.let {
                    Toasty.error(this, it).show()
                }
            }
        }

        viewModel.verifyCodeResult.observe(this) { result ->
            binding.btnNext.isEnabled = result.isSuccessful
            if(result.isSuccessful) {
                moveNext()
            } else {
                Toasty.error(this, result.message!!).show()
            }
        }
    }

    private fun startTimer() {
        binding.btnRequest.text = "재전송"
        isCodeSent = true
        updateValidateButton()

        startTime = System.currentTimeMillis()
        if(timerJob == null) {
            timerJob = tickerFlow(100.toDuration(DurationUnit.MILLISECONDS))
                .map { System.currentTimeMillis() }
                .distinctUntilChanged { old, new -> old == new }
                .onEach {
                    val diff = it - startTime
                    if(diff <= validateTimeMillis) {
                        binding.textTimer.text = formatTimerMilliseconds(validateTimeMillis - diff + 1000)
                    } else {
                        endTimer()
                    }
                }.launchIn(lifecycleScope)
        } else {
            if(!timerJob!!.isActive) {
                timerJob = tickerFlow(100.toDuration(DurationUnit.MILLISECONDS))
                    .map { System.currentTimeMillis() }
                    .distinctUntilChanged { old, new -> old == new }
                    .onEach {
                        val diff = it - startTime
                        if(diff <= validateTimeMillis) {
                            binding.textTimer.text = formatTimerMilliseconds(validateTimeMillis - diff + 1000)
                        } else {
                            endTimer()
                        }
                    }.launchIn(lifecycleScope)
            }
        }
    }

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (isCodeSent) {
            emit(Unit)
            delay(period)
        }
    }

    private fun updateValidateButton() {
        val code = binding.inputCode.text.toString()
        binding.btnValidate.isEnabled = (code.length == 6) && isCodeSent
    }

    private fun endTimer() {
        isCodeSent = false
        binding.btnRequest.isEnabled = true

        binding.textTimer.text = "0:00"
        updateValidateButton()
    }
}