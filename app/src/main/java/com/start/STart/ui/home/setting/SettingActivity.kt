package com.start.STart.ui.home.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.start.STart.api.ApiClient
import com.start.STart.databinding.ActivitySettingBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.start.STart.ui.home.setting.devinfo.DevInfoActivity
import com.start.STart.ui.home.setting.suggest.SuggestActivity
import com.start.STart.ui.home.setting.updatehistory.UpdateHistoryActivity
class SettingActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewListeners()
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
        binding.textFeatureSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_FEATURE)}
        binding.textErrorSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_ERROR)}
        binding.textEtcSuggest.setOnClickListener { startSuggestActivity(SuggestActivity.TYPE_ETC)}

        binding.textLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                ApiClient.disableToken()
                PreferenceManager.clear()

                withContext(Dispatchers.Main) {
                    startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                    finish()
                }
            }
        }
    }

    private fun startSuggestActivity(type: String) {
        startActivity(Intent(this, SuggestActivity::class.java).apply {
            putExtra(SuggestActivity.TYPE_SUGGEST, type)
        })
    }
}