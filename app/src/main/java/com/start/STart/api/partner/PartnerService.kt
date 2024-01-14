package com.start.STart.api.partner

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerService {
    @GET("partner/search")
    suspend fun getList(
        @Query("keyword") keyword: String? = null,
        @Query("categoryId") categoryId: Int? = null,
        @Query("page") page: Int = 0,
        @Query("count") count: Int = 20
    ): Response<ApiResponse>
}