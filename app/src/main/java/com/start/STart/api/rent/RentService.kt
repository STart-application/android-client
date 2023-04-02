package com.start.STart.api.rent

import com.start.STart.api.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RentService {
    @GET("rent/calendar")
    suspend fun getCalendarData(
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("category") category: String,
    ): Response<ApiResponse>


    @GET("rent/item/calendar")
    suspend fun getItemCount(
        @Query("category") category: String,
    ): Response<ApiResponse>

    @GET("rent")
    suspend fun getMyRent(

    ): Response<ApiResponse>
}