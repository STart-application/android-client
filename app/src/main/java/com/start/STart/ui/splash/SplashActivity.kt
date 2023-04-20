package com.start.STart.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        setContentView(binding.root)

        checkAppUpdate()
        lifecycleScope.launch(Dispatchers.IO) {

            val loginTask = async { tryLogin() }
            val loadMemberTask = async(start = CoroutineStart.LAZY) { loadMember() }
            val withOutLoginTask = async { checkAgreeWithoutLogin() }
            val animationTask = launch { checkAnimation() }

            var activity: Class<*>? = null

            if (loginTask.await()) {
                loadMemberTask.start()
                if(loadMemberTask.await().isSuccessful) {
                    PreferenceManager.saveToPreferences(Constants.KEY_MEMBER_DATA, loadMemberTask.await().data as MemberData)
                    activity = HomeActivity::class.java
                } else {
                    activity = if(withOutLoginTask.await()) HomeActivity::class.java else LoginOrSkipActivity::class.java
                }
            } else {
                activity = if(withOutLoginTask.await()) HomeActivity::class.java else LoginOrSkipActivity::class.java
            }


            animationTask.join()
            withContext(Dispatchers.Main) {
                startActivity(Intent(applicationContext, activity))
                finish()
            }
        }

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
                vp.iconView.alpha = 0f
                binding.lottieView.playAnimation()
            }, delay)
        }
    }

    private suspend fun checkAnimation() = suspendCoroutine<Unit> { continuation ->
        binding.lottieView.addAnimatorUpdateListener { animation ->
            if (animation.animatedValue as Float >= 0.6f) {
                binding.lottieView.removeAllUpdateListeners()
                continuation.resume(Unit)
            }
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

    private suspend fun tryLogin(): Boolean {
        ApiClient.disableToken()
        return TokenHelper.tryLoginWithAccessToken()
    }

    private suspend fun loadMember(): ResultModel {
        return MemberDataHelper.readMember()
    }

    //
    private suspend fun checkAgreeWithoutLogin(): Boolean {
        return PreferenceManager.getBoolean(Constants.KEY_AGREE_WITHOUT_LOGIN)
    }
}