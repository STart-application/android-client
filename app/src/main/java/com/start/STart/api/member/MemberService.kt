package com.start.STart.api.member

import com.start.STart.api.ApiResponse
import com.start.STart.api.member.request.ResetPasswordWithLoginBody
import com.start.STart.api.member.request.ResetPasswordWithoutLoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface MemberService {
    @GET("member/duplicate")
    suspend fun checkDuplicate(
        @Query("studentNo") studentId: String,
    ): Response<ApiResponse>

    @POST("member")
    @Multipart
    suspend fun register(
        @PartMap data: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part?
    ): Response<ApiResponse>

    @GET("member")
    suspend fun readMember(

    ): Response<ApiResponse>

    // 로그인 안된 유저 비밀번호 변경
    @PATCH("member/password")
    suspend fun resetPasswordWithoutLogin(
        @Body request: ResetPasswordWithoutLoginRequest
    ): Response<ApiResponse>

    // 로그인된 유저 비밀번호 재설정
    @PATCH("member/login/password")
    suspend fun resetPasswordWithLogin(
        @Body body: ResetPasswordWithLoginBody
    ): Response<ApiResponse>

}