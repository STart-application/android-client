package com.start.STart.api.festival

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface FestivalService {

    @GET("booth")
    suspend fun loadBooth(

    ): Response<ApiResponse>
}