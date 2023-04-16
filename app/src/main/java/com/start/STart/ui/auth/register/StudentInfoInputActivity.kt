package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateStudentInfoBinding
import com.start.STart.util.Constants
import com.start.STart.util.InputTextWater
import es.dmoral.toasty.Toasty

class StudentInfoInputActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentInfoBinding.inflate(layoutInflater) }
    private val viewModel: StudentInfoInputViewModel by viewModels()
    private val textWater = object : InputTextWater() {
        override fun afterTextChanged(p0: Editable?) {
            checkInputIsNotBlank()
        }
    }

    private val selectCollegeOrDepartmentDialog by lazy { SelectCollegeOrDepartmentDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.layoutInputCollege.setOnClickListener {
            selectCollegeOrDepartmentDialog.type = SelectCollegeOrDepartmentDialog.TYPE_COLLEGE
            selectCollegeOrDepartmentDialog.show(supportFragmentManager, ".SelectCollegeDialog")
        }

        binding.layoutInputDepartment.setOnClickListener {
            if(binding.inputStudentCollege.text.isNotBlank()) {
                selectCollegeOrDepartmentDialog.type = SelectCollegeOrDepartmentDialog.TYPE_DEPARTMENT
                selectCollegeOrDepartmentDialog.show(supportFragmentManager, ".SelectCollegeDialog")
            } else {
                Toasty.error(this, "단과대학을 선택해주세요.").show()
            }
        }
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
                startActivity(Intent(this, PasswordInputActivity::class.java).apply {
                    putExtra(
                        Constants.KEY_REGISTER_DATA, RegisterData(
                        studentNo = binding.inputStudentId.text.toString(),
                        name = binding.inputName.text.toString(),
                        department = binding.inputDepartment.text.toString()
                    )
                    )
                })

            } else {
                resultModel.message?.let {
                    Toasty.error(this, it).show()
                }
            }
        }

        viewModel.collegeData.observe(this) {
            binding.inputStudentCollege.text = it
            checkInputIsNotBlank()
        }

        viewModel.departmentData.observe(this) {
            binding.inputDepartment.text = it
            checkInputIsNotBlank()
        }
    }

    private fun checkInputIsNotBlank() {
        val allNotBlank = listOf(binding.inputName, binding.inputStudentId, binding.inputStudentCollege, binding.inputDepartment).all {
            it.text.toString().isNotBlank()
        }
        binding.btnNext.isEnabled = allNotBlank
    }

    private fun checkInputValid(): Boolean {
        if (binding.inputStudentId.text.isNullOrBlank()) { // 도달 불가
            Toasty.error(this, "학번을 입력해주세요.").show()
            return false
        }
        if (binding.inputStudentId.text?.length != 8) {
            Toasty.error(this, "올바른 학번을 입력해주세요.").show()

            return false
        }
        if (binding.inputName.text.isNullOrBlank()) { // 도달 불가
            Toasty.error(this, "이름을 입력해주세요.").show()
            return false
        }
        return true
    }
}