package com.start.STart.api.banner

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface BannerService {
    @GET("banner")
    suspend fun readBanner(): Response<ApiResponse>
}