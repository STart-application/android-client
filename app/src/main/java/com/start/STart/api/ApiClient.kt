package com.start.STart.api

import android.util.Log
import com.start.STart.BuildConfig
import com.start.STart.api.auth.AuthService
import com.start.STart.api.banner.BannerService
import com.start.STart.api.banner.EventService
import com.start.STart.api.festival.FestivalService
import com.start.STart.api.member.MemberService
import com.start.STart.api.rent.RentService
import com.start.STart.api.stamp.StampService
import com.start.STart.api.suggestion.SuggestionService
import com.start.STart.util.gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val tokenInterceptor = TokenInterceptor()

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .build()

     private val retrofit: Retrofit by lazy {
        Log.d("Retrofit", "${BuildConfig.DEV_SERVER_URL}")
        Retrofit.Builder()
            .baseUrl(BuildConfig.DEV_SERVER_URL) // 주소 끝에 반드시 슬래시 / 로 끝나야 함
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val authService: AuthService = retrofit.create(AuthService::class.java)
    val memberService: MemberService = retrofit.create(MemberService::class.java)
    val bannerService: BannerService = retrofit.create(BannerService::class.java)
    val suggestionService: SuggestionService = retrofit.create(SuggestionService::class.java)
    val rentService: RentService = retrofit.create(RentService::class.java)
    val festivalService: FestivalService = retrofit.create(FestivalService::class.java)
    val eventService: EventService = retrofit.create(EventService::class.java)
    val stampService: StampService = retrofit.create(StampService::class.java)


    fun enableToken(token: String) { // TokenHelper에서만 호출
        tokenInterceptor.enableToken(token)
    }
    fun disableToken() { // TokenHelper에서만 호출
        tokenInterceptor.disableToken()
    }

    fun parseErrorBody(errorBodyString: String?): ApiResponse {
        val body = gson.fromJson(errorBodyString, ApiResponse::class.java)
        return body
    }


}