package com.start.STart.api.stamp

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface StampService {

    @GET("stamp")
    suspend fun loadStamp(

    ): Response<ApiResponse>
}