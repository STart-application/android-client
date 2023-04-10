package com.start.STart.ui.splash

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.start.STart.api.ApiClient
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivitySplashBinding
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.MemberDataHelper
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val updateCode = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        
        // 로그인 시도
        checkAppUpdate()
        lifecycleScope.launch(Dispatchers.IO) {
            tryLogin()
        }

        // TODO: 로그인 로직 정상 동작 확인 후 버튼 삭제
        binding.btnMove.setOnClickListener {
            startActivity(Intent(this, LoginOrSkipActivity::class.java))
            finish()
        }
    }

    private fun checkAppUpdate() {
        appUpdateManager.appUpdateInfo.addOnCompleteListener {
            if(it.isSuccessful) {
                if(it.result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(it.result, AppUpdateType.IMMEDIATE, this, updateCode)
                        Log.d(null, "checkAppUpdate: 업데이트 시작")
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.d(null, "checkAppUpdate: 업데이트 없음")
                }
            } else {
                Log.d(null, "checkAppUpdate: ${it.exception}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == updateCode && resultCode != RESULT_OK) {
            Log.d(null, "onActivityResult: 성공")
        } else {
            Log.d(null, "onActivityResult: 실패")
        }
    }

    private suspend fun tryLogin()  {
        ApiClient.disableToken()
        val isSuccessful = TokenHelper.tryLoginWithAccessToken()
        if(isSuccessful) {
            loadMember()
        } else {
            checkAgreeWithoutLogin()
        }
    }

    private suspend fun loadMember()  {
        val result = MemberDataHelper.readMember()
        if(result.isSuccessful) {
            PreferenceManager.saveToPreferences(Constants.KEY_MEMBER_DATA, result.data as MemberData)
            withContext(Dispatchers.Main) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        } else {
            checkAgreeWithoutLogin()
        }
    }

    //
    private suspend fun checkAgreeWithoutLogin() {
        if(PreferenceManager.getBoolean(Constants.KEY_AGREE_WITHOUT_LOGIN)) {
            withContext(Dispatchers.Main) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        } else {
            startActivity(Intent(applicationContext, LoginOrSkipActivity::class.java))
            finish()
        }
    }
}