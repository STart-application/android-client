package com.start.STart.api.member

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MemberService {
    @GET("member/duplicate")
    suspend fun checkDuplicate(
        @Query("studentNo") studentId: String,
    ): Response<ApiResponse>
}