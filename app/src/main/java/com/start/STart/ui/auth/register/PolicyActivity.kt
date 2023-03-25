package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
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

        initViewListeners()
    }

    private fun initViewListeners() {
        updateCheckAllCheckBox()
        linkPolicy()
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, StudentInfoInputActivity::class.java))
        }
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "약관동의"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun linkPolicy() {
        binding.textMorePrivacy.setOnClickListener {
            openCustomTab(resources.getString(R.string.link_privacy_policy))
        }

        binding.textMoreService.setOnClickListener {
            openCustomTab(resources.getString(R.string.link_terms_of_service))
        }
    }

    private fun updateCheckAllCheckBox() {
        val checkBoxList = listOf(binding.checkPrivacyPolicy, binding.checkService)

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