package com.start.STart.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.start.STart.BuildConfig
import com.start.STart.api.auth.AuthService
import com.start.STart.api.member.MemberService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Log.d("Retrofit", "${BuildConfig.DEV_SERVER_URL}")
        Retrofit.Builder()
            .baseUrl(BuildConfig.DEV_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val authService = retrofit.create(AuthService::class.java)
    val memberService = retrofit.create(MemberService::class.java)
}