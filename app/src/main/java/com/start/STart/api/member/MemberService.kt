package com.start.STart.api.member

import com.start.STart.api.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
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
        @Part file: MultipartBody.Part
    ): Response<ApiResponse>
}