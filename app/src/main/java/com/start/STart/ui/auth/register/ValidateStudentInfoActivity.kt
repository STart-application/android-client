package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.start.STart.databinding.ActivityValidateStudentInfoBinding

class ValidateStudentInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentInfoBinding.inflate(layoutInflater) }
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
            startActivity(Intent(this, ValidatePasswordActivity::class.java))
        }
    }

    private fun checkInput(): String? {
        // TODO: 학번 공백 확인
        if(binding.inputStudentId.text.isNullOrBlank()) {
            return "학번을 입력해주세요."
        }
        // TODO: 학번 자리수 8 확인
        if(binding.inputStudentId.text?.length != 8) {
            return "올바르 학번을 입력해주세요."
        }
        // TODO: 학번 중복 확인 (API)

        // TODO: 이름 공백 확인
        if(binding.inputName.text.isNullOrBlank()) {
            return "이름을 입력해주세요."
        }
        return null
    }
}