package com.start.STart.api.festival

import com.start.STart.api.ApiResponse
import com.start.STart.api.festival.response.LoadBoothResponse
import com.start.STart.api.festival.response.FoodTruckModel
import com.start.STart.api.festival.response.PhotoZoneModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface FestivalService {

    @GET("festival")
    suspend fun checkFestivalPeriod(

    ): Response<ApiResponse>

    @GET("booth")
    suspend fun loadBooth(

    ): Response<LoadBoothResponse>

    @GET("food-trucks")
    fun loadFoodTruck(): Call<FoodTruckModel>

    @GET("photo-zones")
    fun loadPhotoZones(): Call<PhotoZoneModel>
}