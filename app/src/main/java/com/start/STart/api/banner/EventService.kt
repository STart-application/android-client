package com.start.STart.api.banner

import retrofit2.Call
import retrofit2.http.GET

interface EventService {
    @GET("event")
    fun loadEvent(): Call<EventModel>
}