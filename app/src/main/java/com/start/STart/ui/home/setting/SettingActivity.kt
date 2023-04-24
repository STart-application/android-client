package com.start.STart.ui.home.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
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
import com.start.STart.util.getCollegeByDepartment
import com.start.STart.util.openCustomTab
import java.io.File

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
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                PreferenceManager.clear()
                withContext(Dispatchers.Main) {
                    startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                    finish()
                }
            }
        }

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


        /*
        binding.textPrivacyPolicy.setOnClickListener{
            val intent = Intent(Intent.ACTION_DEFAULT)
            val file = File("android.resource://com.start.STart/${R.raw.sc_privacy}")
            if(file.exists()) {
                val uri = Uri.fromFile(file)
                intent.data = uri
                startActivity(intent)
            }
        }

        binding.textTermsOfService.setOnClickListener {
            val pdfUri = Uri.parse("android.resource://com.start.STart/${R.raw.sc_service}")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfUri, "application")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            Log.d("tag", "service")
        }

         */
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

        binding.toolbar.btnBack.setOnClickListener { finish() }
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
                    binding.textCollege.text = getCollegeByDepartment(it.department)
                    binding.textDepartment.text = it.department
                }
            } else {
                disableAuthManagement()
            }
        }
    }

    private fun disableAuthManagement() {
        binding.btnLogin.visibility = View.VISIBLE
        binding.layoutAuthManagement.alpha = 0.7f
        binding.layoutAuthManagement.children.forEach {
            it.isEnabled = false
        }
    }

    private fun startSuggestActivity(type: String) {
        startActivity(Intent(this, SuggestActivity::class.java).apply {
            putExtra(SuggestActivity.KEY_TYPE_SUGGEST, type)
        })
    }
}