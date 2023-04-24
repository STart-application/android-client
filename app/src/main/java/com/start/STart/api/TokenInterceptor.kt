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
            .header(Constants.KEY_AUTHORIZATION, "Bearer ${token}")
            .method(original.method, original.body)
            .build()
        Log.d(TAG, "intercept: $token")

        var response = chain.proceed(request)

        // 만료된 토큰 관련 처리
        val body = ApiClient.parseBody(response.peekBody(Long.MAX_VALUE).string())
        Log.d(TAG, "intercept: 복제 $body")

        if(body.errorCode == ApiError.ST011.name) {
            Log.d(TAG, "intercept: 토큰 만료료 인한 재요청 시작")
            val result = runBlocking { issueAccessToken(token) }

            if(result.isSuccessful) {
                Log.d(TAG, "intercept: 토큰 만료료 인한 재요청 성공")

                Log.d(TAG, "intercept: 새로운 토큰 ${result.data as String}")
                request = original.newBuilder()
                    .header(Constants.KEY_AUTHORIZATION, "Bearer ${result.data as String}")
                    .method(original.method, original.body)
                    .build()

                response.close() // 첫 번째 요청으로 받은 응답을 닫아준다.
                response = chain.proceed(request) // 수정된 요청을 다시 보냄
            } else {
                Log.d(TAG, "intercept: 토큰 만료료 인한 재요청 실패")
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
                //val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                // 무조건 로그아웃
                ApiClient.disableToken()
                runBlocking(Dispatchers.Main) {
                    PreferenceManager.clear()
                    val context = MyApp.getAppContext()
                    context.startActivity(Intent(context, LoginOrSkipActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ResultModel(false)
    }
}