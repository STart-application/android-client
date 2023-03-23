package com.start.STart.ui.home.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.start.STart.R
import com.start.STart.api.ApiClient
import com.start.STart.databinding.ActivitySettingBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.start.STart.ui.home.setting.devinfo.DevInfoActivity
import com.start.STart.ui.home.setting.suggest.SuggestActivity
import com.start.STart.ui.home.setting.updatehistory.UpdateHistoryActivity
import com.start.STart.util.openCustomTab

class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewListeners()
        initViewModelListeners()
    }

    private fun init() {
        viewModel.loadMemberData()
    }

    private fun initView() {
        binding.toolbar.textTitle.text = "설정"
    }

    private fun initViewListeners() {
        binding.textUpdateLog.setOnClickListener {
            startActivity(Intent(this, UpdateHistoryActivity::class.java))
        }
        binding.textDevInfo.setOnClickListener {
            startActivity(Intent(this, DevInfoActivity::class.java))
        }
        // SNS 연결
        binding.layoutInstagram.setOnClickListener { openCustomTab(resources.getString(R.string.link_instagram)) }
        binding.layoutHomePage.setOnClickListener { openCustomTab(resources.getString(R.string.link_st_home)) }
        binding.layoutYoutube.setOnClickListener { openCustomTab(resources.getString(R.string.link_youtube)) }
        binding.layoutKakaoTalk.setOnClickListener { openCustomTab(resources.getString(R.string.link_kakao_talk)) }

        // 제안사항
        binding.textFeatureSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_FEATURE)}
        binding.textErrorSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_ERROR)}
        binding.textEtcSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_ETC)}

        binding.textPrivacyPolicy.setOnClickListener { openCustomTab(resources.getString(R.string.link_privacy_policy)) }
        binding.textTermsOfService.setOnClickListener { openCustomTab(resources.getString(R.string.link_terms_of_service)) }
        binding.textOpenSourceLicense.setOnClickListener {
            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }

        // 로그아웃
        binding.textLogout.setOnClickListener {
            logout()
        }

        binding.toolbar.icBack.setOnClickListener { finish() }
    }

    private fun logout() = lifecycleScope.launch(Dispatchers.IO) {

        ApiClient.disableToken()
        PreferenceManager.clear()

        withContext(Dispatchers.Main) {
            startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            finish()
        }
    }
    private fun initViewModelListeners() {
        viewModel.memberData.observe(this) { memberData ->
            if(memberData != null){
                memberData.let {
                    binding.textName.text = it.name
                    binding.textStudentId.text = it.studentNo
                    binding.textDepartment.text = it.department
                }
            } else {
                logout()
            }
        }
    }

    private fun startSuggestActivity(type: String) {
        startActivity(Intent(this, SuggestActivity::class.java).apply {
            putExtra(SuggestActivity.TYPE_SUGGEST, type)
        })
    }
}