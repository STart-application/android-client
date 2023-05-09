package com.start.STart.ui.splash

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.start.STart.api.ApiClient
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivitySplashBinding
import com.start.STart.model.ResultModel
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.MemberDataHelper
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Clock
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private val updateCode = 99

    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        setContentView(binding.root)

        splashScreen.setOnExitAnimationListener { vp ->
            binding.lottieView.enableMergePathsForKitKatAndAbove(true)

            val splashScreenAnimationEndTime =
                Instant.ofEpochMilli(vp.iconAnimationStartMillis + vp.iconAnimationDurationMillis)
            val delay = Instant.now(Clock.systemUTC()).until(
                splashScreenAnimationEndTime,
                ChronoUnit.MILLIS
            )


            binding.lottieView.postDelayed({
                vp.view.alpha = 0f
                try {
                    vp.iconView.alpha = 0f
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
                binding.lottieView.playAnimation()
            }, delay)
        }


        lifecycleScope.launch {
            val updateEnabled = withContext(Dispatchers.IO) { checkAppUpdate() }
            if(!updateEnabled) {
                startMainTask()
            }
        }
    }

    private fun startMainTask() = lifecycleScope.launch(Dispatchers.IO) {
        val loginTask = async { tryLogin() }
        val loadMemberTask = async(start = CoroutineStart.LAZY) { loadMember() }
        val withOutLoginTask = async { checkAgreeWithoutLogin() }
        val animationTask = launch { checkAnimation() }

        var activity: Class<*>? = null

        if (loginTask.await()) {
            Log.d(null, "startMainTask: 로그인 성공")
            loadMemberTask.start()
            if (loadMemberTask.await().isSuccessful) {
                PreferenceManager.saveToPreferences(
                    Constants.PREF_KEY_MEMBER_DATA,
                    loadMemberTask.await().data as MemberData
                )
                activity = HomeActivity::class.java
            } else {
                PreferenceManager.remove(Constants.PREF_KEY_MEMBER_DATA)
                activity =
                    if (withOutLoginTask.await()) HomeActivity::class.java else LoginOrSkipActivity::class.java
            }
        } else {
            Log.d(null, "startMainTask: 로그인 실패")
            PreferenceManager.remove(Constants.PREF_KEY_MEMBER_DATA)
            activity =
                if (withOutLoginTask.await()) HomeActivity::class.java else LoginOrSkipActivity::class.java
        }

        Log.d(null, "startMainTask: 모든 작업 끝")
        animationTask.join()
        Log.d(null, "startMainTask: 애니메이션 작업 끝")
        withContext(Dispatchers.Main) {
            Log.d(null, "startMainTask: 액티비티 시작")
            startActivity(Intent(applicationContext, activity))
            finish()
        }
    }




    private suspend fun checkAnimation() = suspendCoroutine<Unit> { continuation ->
        if(!binding.lottieView.isAnimating) {
            continuation.resume(Unit)
            return@suspendCoroutine
        }
        binding.lottieView.addAnimatorUpdateListener { animation ->
            Log.d(null, "checkAnimation: ${animation.animatedValue}")
            if (animation.animatedValue as Float >= 0.6f) {
                binding.lottieView.removeAllUpdateListeners()
                continuation.resume(Unit)
            }
        }
    }

    private suspend fun checkAppUpdate() = suspendCoroutine { continuation ->
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, updateCode)
                    continuation.resume(true)
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                    continuation.resume(false)
                }
            } else {
                Log.d(null, "checkAppUpdate: 업데이트 없음")
                continuation.resume(false)
            }
        }.addOnFailureListener {
            it.printStackTrace()
            continuation.resume(false)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == updateCode) {
            Log.d(null, "onActivityResult: $updateCode")
            startMainTask()
        }

    }

    private suspend fun tryLogin(): Boolean {
        ApiClient.disableToken()
        return TokenHelper.tryLoginWithAccessToken()
    }

    private suspend fun loadMember(): ResultModel {
        return MemberDataHelper.readMember()
    }

    //
    private suspend fun checkAgreeWithoutLogin(): Boolean {
        return PreferenceManager.getBoolean(Constants.PREF_FLAG_WITHOUT_LOGIN)
    }
}