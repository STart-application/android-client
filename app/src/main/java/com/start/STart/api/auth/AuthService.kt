package com.start.STart.api.auth

import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    // 로그인
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse>

    // 로그아웃
    @GET("/api/auth/logout")
    suspend fun logout(
        @Header("refresh") token: String,
    ): Response<ApiResponse>

    // 휴대폰 인증 요청
    @POST("/api/auth/sms")
    suspend fun sendSmsCode(
        @Body request: SendSmsCodeRequest
    ): Response<ApiResponse>

    // 휴대폰 인증 확인
    @POST("/api/v1/auth/sms/check")
    suspend fun verifySmsCode(
        @Body request: VerifySmsCodeRequest
    ): Response<ApiResponse>

    // 비밀번호 찾기 인증 요청
    @POST("/api/auth/sms/password")
    suspend fun sendResetPasswordCode(
        @Body request: SendResetPasswordCodeRequest
    ): Response<ApiResponse>

    // 비밀번호 찾기 인증 확인
    @POST("/api/auth/sms/password/check")
    suspend fun verifyResetPasswordCode(
        @Body request: VerifyResetPasswordCode
    ): Response<ApiResponse>
}