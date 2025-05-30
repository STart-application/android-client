package com.start.STart.api.auth

import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.*
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    // 로그인
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse>

    // 로그아웃
    @GET("auth/logout")
    suspend fun logout(
        @Header("Refresh") token: String,
    ): Response<ApiResponse>

    // 휴대폰 인증 요청
    @POST("auth/sms")
    suspend fun sendSmsCode(
        @Body request: SendSmsCodeRequest
    ): Response<ApiResponse>

    // 휴대폰 인증 확인
    @POST("auth/sms/check")
    suspend fun verifySmsCode(
        @Body request: VerifySmsCodeRequest
    ): Response<ApiResponse>

    // 비밀번호 찾기 인증 요청
    @POST("auth/sms/password")
    suspend fun sendResetPasswordCode(
        @Body request: SendResetPasswordCodeRequest
    ): Response<ApiResponse>

    // 비밀번호 찾기 인증 확인
    @POST("auth/sms/password/check")
    suspend fun verifyResetPasswordCode(
        @Body request: VerifyResetPasswordCode
    ): Response<ApiResponse>

    // AccessToken 확인
    @GET("auth")
    suspend fun verifyAccessToken(
        @Header("Authorization") accessToken: String,
    ): Response<ApiResponse>

    // AccessToken 재발급 -> 만료된 AccessToken과 함께 호출해야 함
    @GET("auth/refresh")
    suspend fun issueAccessToken(
        @Header("Authorization") accessToken: String,
        @Header("Refresh") refreshToken: String,
    ): Response<ApiResponse>
}