package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import com.start.STart.R
import com.start.STart.databinding.ActivityPolicyBinding
import com.start.STart.util.openCustomTab

class PolicyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        initToolbar()
        initPolicyCustomTab()
        initCheckBox()

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, StudentInfoInputActivity::class.java))
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "약관동의"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initPolicyCustomTab() {
        binding.textMorePrivacy.setOnClickListener {
            openCustomTab(resources.getString(R.string.link_privacy_policy))
        }

        binding.textMoreService.setOnClickListener {
            openCustomTab(resources.getString(R.string.link_terms_of_service))
        }
    }

    private fun initCheckBox() {
        val checkBoxList = listOf(binding.checkPrivacyPolicy, binding.checkService)

        binding.checkAll.setOnClickListener {
            updateSubCheckBox(checkBoxList, binding.checkAll.isChecked)
        }

        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }

        initSubCheckBox(checkBoxList)
    }

    private fun initSubCheckBox(checkBoxList: List<AppCompatCheckBox>) {
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

    private fun updateSubCheckBox(checkBoxList: List<CheckBox>, isChecked: Boolean) {
        binding.checkAll.isChecked = isChecked
        checkBoxList.forEach { it.isChecked = isChecked }
    }
}