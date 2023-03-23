package com.start.STart.api

import android.util.Log
import com.start.STart.util.Constants
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
        val request = original.newBuilder()
            .header(Constants.KEY_AUTHORIZATION, "Bearer ${token!!}")
            .method(original.method, original.body)
            .build()
        Log.d(TAG, "intercept: $token")

        return chain.proceed(request)
    }
}