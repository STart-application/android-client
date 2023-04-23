package com.start.STart.ui.home.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.start.STart.R
import com.start.STart.api.ApiClient
import com.start.STart.databinding.ActivitySettingBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.ui.home.setting.devinfo.DevInfoActivity
import com.start.STart.ui.home.setting.reset.ResetPasswordWithLoginActivity
import com.start.STart.ui.home.setting.suggest.SuggestActivity
import com.start.STart.ui.home.setting.updatehistory.UpdateHistoryActivity
import com.start.STart.util.PreferenceManager
import com.start.STart.util.getCollegeByDepartment
import com.start.STart.util.openCustomTab
import com.start.STart.util.showErrorToast
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    private val viewModel: SettingViewModel by viewModels()

    private val confirmDialog by lazy { ConfirmDialog() }
    var deleteMemberClickCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()



        initViewListeners()
        initViewModelListeners()

        viewModel.loadMemberData()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "설정"
        binding.toolbar.btnBack.setOnClickListener { finish() }
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

        binding.textResetPassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordWithLoginActivity::class.java))
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

        binding.textPrivacyPolicy.setOnClickListener { openCustomTab(resources.getString(R.string.link_privacy_policy)) }
        binding.textTermsOfService.setOnClickListener { openCustomTab(resources.getString(R.string.link_terms_of_service)) }
        binding.textOpenSourceLicense.setOnClickListener {
            OssLicensesMenuActivity.setActivityTitle("오픈소스 라이선스")
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }

        // 로그아웃
        binding.textLogout.setOnClickListener {
            if(!confirmDialog.isAdded) {
                confirmDialog.setData("로그아웃 하시겠습니까?", onConfirm = {
                    logout()
                })
                confirmDialog.show(supportFragmentManager, null)
            }
        }

        // 회원탈퇴
        binding.textDeleteMember.setOnClickListener {
            if(!confirmDialog.isAdded) {
                confirmDialog.setData("회원 탈퇴 시 재가입이 어려울 수 있습니다. (문의 : 02-970-7012)",
                    onConfirm = {
                        deleteMemberClickCnt += 1
                        if(deleteMemberClickCnt == 2) {
                            viewModel.deleteMember()
                        } else {
                            Toasty.info(this, "다시 한 번 클릭해주세요.").show()
                        }
                    }, onCancel = {
                        deleteMemberClickCnt = 0
                        confirmDialog.dismiss()
                    }
                )
                confirmDialog.show(supportFragmentManager, null)
            }
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
            //finish()
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

        viewModel.deleteMemberResult.observe(this) { result ->
            if(result.isSuccessful) {
                startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            } else {
                showErrorToast(this, result.message)
            }
        }
    }

    private fun disableAuthManagement() {
        binding.btnLogin.visibility = View.VISIBLE
        binding.textLoginRequired.visibility = View.VISIBLE

        //binding.layoutAuthManagement.alpha = 0.7f
        binding.layoutAuthManagement.children.forEach {
            if(it is TextView) {
                it.setTextColor(ContextCompat.getColor(this, R.color.text_caption))
            }
            it.isEnabled = false
        }
    }

    private fun startSuggestActivity(type: String) {
        startActivity(Intent(this, SuggestActivity::class.java).apply {
            putExtra(SuggestActivity.KEY_TYPE_SUGGEST, type)
        })
    }
}