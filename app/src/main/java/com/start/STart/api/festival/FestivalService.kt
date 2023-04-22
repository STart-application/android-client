package com.start.STart.api.festival

import com.start.STart.api.ApiResponse
import com.start.STart.api.festival.response.LoadBoothResponse
import retrofit2.Response
import retrofit2.http.GET

interface FestivalService {

    @GET("festival")
    suspend fun checkFestivalPeriod(

    ): Response<ApiResponse>

    @GET("booth")
    suspend fun loadBooth(

    ): Response<LoadBoothResponse>
}