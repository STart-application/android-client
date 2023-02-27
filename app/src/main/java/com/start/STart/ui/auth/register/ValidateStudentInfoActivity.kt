package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.dp
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.TextForm
import com.start.STart.R
import com.start.STart.databinding.ActivityValidateStudentInfoBinding

class ValidateStudentInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentInfoBinding.inflate(layoutInflater) }
    private val viewModel: ValidateStudentInfoViewModel by viewModels()
    private val textWater = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val allNotBlank = listOf(binding.inputName, binding.inputStudentId).all { it.text.toString().isNotBlank() }
            binding.btnNext.isEnabled = allNotBlank
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewListeners()
        initViewModelListeners()
    }

    private fun initView() {
        binding.inputDepartment.selectItemByIndex(0)
        binding.inputName.addTextChangedListener(textWater)
        binding.inputStudentId.addTextChangedListener(textWater)
    }
    private fun initViewListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            if(checkInputValid()) {
                viewModel.checkDuplicate(binding.inputStudentId.text.toString())
            }
        }
    }

    private fun initViewModelListeners() {
        viewModel.isDuplicate.observe(this) { isDuplicate ->
            if(!isDuplicate) {
                startActivity(Intent(this, ValidatePasswordActivity::class.java))
            } else {
                Balloon.Builder(this)
                    .setText("같은 학번으로 가입된 계정이 존재합니다.")
                    .build()
                    .showAlignTop(binding.inputStudentId)
            }
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, "에러: $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkInputValid(): Boolean { // TODO: 에러 표시 뷰 XML에 추가해야 함
        if(binding.inputStudentId.text.isNullOrBlank()) { // 도달 불가
            Balloon.Builder(this)
                .setText("학번을 입력해주세요.")
                .build()
                .showAlignTop(binding.inputStudentId)
            return false
        }
        if(binding.inputStudentId.text?.length != 8) {
            getErrorBalloon("올바른 학번을 입력해주세요.")
                .showAlignTop(binding.inputStudentId)

            return false
        }
        if(binding.inputName.text.isNullOrBlank()) { // 도달 불가
            getErrorBalloon("이름을 입력해주세요.")
                .showAlignTop(binding.inputName)
            return false
        }
        return true
    }

    private fun getErrorBalloon(message: String): Balloon {
        return Balloon.Builder(this)
            .setText(message)
            .setPadding(8)
            .setBackgroundColor(resources.getColor(R.color.dream_purple))
            .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            .setArrowPosition(0.2f)
            .setAutoDismissDuration(2000L)
            .build()

    }
}