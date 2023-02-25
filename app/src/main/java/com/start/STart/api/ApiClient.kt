package com.start.STart.api

import com.google.gson.GsonBuilder
import com.start.STart.BuildConfig
import com.start.STart.api.auth.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.DEV_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val authService = retrofit.create(AuthService::class.java)
}