package com.start.STart.api

import android.util.Log
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    companion object {
        const val TAG = ".TokenInterceptor"
    }

    var isInterceptorEnabled = false
    private var token: String? = null

    fun enableToken(token: String) {
        isInterceptorEnabled = true
        this.token = token
        Log.d(".TokenInterceptor", "enableToken: ${token}")
    }

    fun disableToken() {
        isInterceptorEnabled = false
        token = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept: $isInterceptorEnabled")
        if (!isInterceptorEnabled) {
            return chain.proceed(chain.request())
        }
        val original = chain.request()
        var request = original.newBuilder()
            .header(Constants.KEY_AUTHORIZATION, "Bearer ${token!!}")
            .method(original.method, original.body)
            .build()
        Log.d(TAG, "intercept: $token")

        var response = chain.proceed(request)

        // 만료된 토큰 관련 처리
        val body = ApiClient.parseBody(response.peekBody(Long.MAX_VALUE).string())
        Log.d(TAG, "intercept: $body")

        if(body.errorCode == ApiError.ST011.name) {
            val isSuccessful = runBlocking { TokenHelper.issueAccessToken(token!!) }
            if(isSuccessful) {
                Log.d(TAG, "intercept: 토큰 만료료 인한 재요청")
                val newToken = PreferenceManager.getString(Constants.KEY_ACCESS_TOKEN)
                request = original.newBuilder()
                    .header(Constants.KEY_AUTHORIZATION, "Bearer ${newToken}")
                    .method(original.method, original.body)
                    .build()

                response = chain.proceed(request)
            }

        }

        return response
    }
}