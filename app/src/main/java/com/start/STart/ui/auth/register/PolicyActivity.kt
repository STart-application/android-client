package com.start.STart.ui.auth.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CheckBox
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.start.STart.R
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
        linkPolicy()
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, ValidateStudentInfoActivity::class.java))
        }
        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }
    }

    private fun linkPolicy() {
        binding.textMorePrivacy.setOnClickListener {
            openCustomTab(resources.getString(R.string.privacy_policy_link))
        }

        binding.textMoreService.setOnClickListener {
            openCustomTab(resources.getString(R.string.service_and_term_link))
        }
    }

    private fun openCustomTab(url: String) {
        CustomTabsIntent.Builder()
            .setInitialActivityHeightPx(500)
            .build()
            .launchUrl(this, Uri.parse(url))
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