package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewListeners()
    }

    private fun initViewListeners() {
        updateCheckAllCheckBox()
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ValidateStudentInfoActivity::class.java))
        }
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }
    }

    private fun updateCheckAllCheckBox() {
        val checkBoxList = listOf(binding.checkPrivacyPolicy, binding.checkService, binding.checkLocation)

        binding.checkAll.setOnClickListener {
            checkAllCheckBoxes(checkBoxList, binding.checkAll.isChecked)
        }

        checkBoxList.forEach {
            it.setOnCheckedChangeListener { _, _ ->
                if (!it.isChecked) {
                    binding.checkAll.isChecked = false
                } else {
                    val allChecked = checkBoxList.all { it.isChecked }
                    binding.checkAll.isChecked = allChecked
                }
            }
        }
    }

    private fun checkAllCheckBoxes(checkBoxList: List<CheckBox>, isChecked: Boolean) {
        binding.checkAll.isChecked = isChecked
        checkBoxList.forEach { it.isChecked = isChecked }
    }
}