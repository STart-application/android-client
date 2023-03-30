package com.start.STart.api.suggestion

import com.start.STart.api.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface SuggestionService {
    @POST("suggestion")
    @Multipart
    suspend fun suggest(
        @PartMap data: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse>
}