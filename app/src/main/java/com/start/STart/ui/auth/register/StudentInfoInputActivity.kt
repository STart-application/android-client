package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.start.STart.R
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateStudentInfoBinding
import com.start.STart.util.Constants
import com.start.STart.util.InputTextWater

class StudentInfoInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentInfoBinding.inflate(layoutInflater) }
    private val viewModel: StudentInfoInputViewModel by viewModels()
    private val textWater = object : InputTextWater() {
        override fun afterTextChanged(p0: Editable?) {
            val allNotBlank = listOf(binding.inputName, binding.inputStudentId).all {
                it.text.toString().isNotBlank()
            }
            binding.btnNext.isEnabled = allNotBlank
        }
    }

    private val selectCollegeDialog by lazy { SelectCollegeDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.inputDepartment.selectItemByIndex(0)
        binding.layoutInputCollege.setOnClickListener { selectCollegeDialog.show(supportFragmentManager, ".SelectCollegeDialog") }
        binding.inputName.addTextChangedListener(textWater)
        binding.inputStudentId.addTextChangedListener(textWater)

        initToolbar()
        initViewListeners()
        initViewModelListeners()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "학적 정보 입력"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {

        binding.btnNext.setOnClickListener {
            if (checkInputValid()) {
                viewModel.checkDuplicate(binding.inputStudentId.text.toString())
            }
        }
    }

    private fun initViewModelListeners() {
        viewModel.verifyDuplicateResult.observe(this) { resultModel ->
            if (resultModel.isSuccessful) {
                // TODO: 부서 수정
                startActivity(Intent(this, PasswordInputActivity::class.java).apply {
                    putExtra(
                        Constants.KEY_REGISTER_DATA, RegisterData(
                        studentNo = binding.inputStudentId.text.toString(),
                        name = binding.inputName.text.toString(),
                        department = "임시부서"
                    )
                    )
                })

            } else {
                // TODO: 처리
            }
        }
    }

    private fun checkInputValid(): Boolean { // TODO: 에러 표시 뷰 XML에 추가해야 함
        if (binding.inputStudentId.text.isNullOrBlank()) { // 도달 불가
            Balloon.Builder(this)
                .setText("학번을 입력해주세요.")
                .build()
                .showAlignTop(binding.inputStudentId)
            return false
        }
        if (binding.inputStudentId.text?.length != 8) {
            getErrorBalloon("올바른 학번을 입력해주세요.")
                .showAlignTop(binding.inputStudentId)

            return false
        }
        if (binding.inputName.text.isNullOrBlank()) { // 도달 불가
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