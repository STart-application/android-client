package com.start.STart.api

import com.start.STart.util.Constants
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    private var isInterceptorEnabled = false
    private var token: String? = null

    fun enableToken(token: String) {
        isInterceptorEnabled = true
        this.token = token
    }

    fun disableToken() {
        isInterceptorEnabled = false
        token = null
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInterceptorEnabled) {
            return chain.proceed(chain.request())
        }
        val original = chain.request()
        val request = original.newBuilder()
            .header(Constants.KEY_AUTHORIZATION, token!!)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}