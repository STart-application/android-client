package com.start.STart.api

import android.content.Intent
import android.util.Log
import com.start.STart.MyApp
import com.start.STart.api.auth.response.TokenData
import com.start.STart.model.ResultModel
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {
    companion object {
        const val TAG = ".TokenInterceptor"
    }

    fun enableToken() {
        PreferenceManager.putBoolean(Constants.FLAG_TOKEN_ENABLED, true)
    }

    fun disableToken() {
        PreferenceManager.putBoolean(Constants.FLAG_TOKEN_ENABLED, true)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val isInterceptorEnabled = PreferenceManager.getBoolean(Constants.FLAG_TOKEN_ENABLED)
        if (!isInterceptorEnabled) {
            return chain.proceed(chain.request())
        }

        val token = PreferenceManager.getString(Constants.KEY_ACCESS_TOKEN)

        val original = chain.request()
        var request = original.newBuilder()
            .header(Constants.KEY_AUTHORIZATION, "Bearer $token")
            .method(original.method, original.body)
            .build()

        var response = chain.proceed(request)

        val body = ApiClient.parseBody(response.peekBody(Long.MAX_VALUE).string())

        if(body.status == 401) { // Access 토큰 관련 오류
            val result = runBlocking { issueAccessToken(token) }

            if(result.isSuccessful) {
                request = original.newBuilder()
                    .header(Constants.KEY_AUTHORIZATION, "Bearer ${result.data as String}")
                    .method(original.method, original.body)
                    .build()

                response.close()
                response = chain.proceed(request)
            } else {
                logout()
            }
        }

        return response
    }

    private suspend fun issueAccessToken(legacyToken: String): ResultModel {
        try {
            val res = ApiClient.authService.issueAccessToken(
                accessToken = "Bearer $legacyToken",
                refreshToken = "Bearer ${PreferenceManager.getString(Constants.KEY_REFRESH_TOKEN)}"
            )

            if(res.isSuccessful) {
                val newAccessToken = res.body()?.parseData(TokenData::class.java)?.accessToken!!
                PreferenceManager.putString(Constants.KEY_ACCESS_TOKEN, newAccessToken)
                ApiClient.enableToken()
                return ResultModel(true, data = newAccessToken)
            } else {
                return ResultModel(false)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ResultModel(false)
    }

    private fun logout() {
        Log.d(null, "로그아웃")
        PreferenceManager.clear()
        runBlocking(Dispatchers.Main) {
            val context = MyApp.getAppContext()
            context.startActivity(Intent(context, LoginOrSkipActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}