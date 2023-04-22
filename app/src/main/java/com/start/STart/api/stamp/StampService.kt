package com.start.STart.api.stamp

import com.start.STart.api.ApiResponse
import com.start.STart.api.stamp.request.PostStampBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StampService {

    @GET("stamp")
    suspend fun loadStamp(

    ): Response<ApiResponse>

    @POST("stamp")
    suspend fun postStamp(
        @Body body: PostStampBody,
    ): Response<ApiResponse>
}