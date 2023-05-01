package com.start.STart.api.event

import com.start.STart.api.banner.EventModel
import com.start.STart.api.event.vote.AnswerRequest
import com.start.STart.api.event.vote.AnswerResponse
import com.start.STart.api.event.vote.QuestionModel
import com.start.STart.api.event.vote.UserStatusModel
import com.start.STart.api.event.vote.VoteModel
import com.start.STart.api.event.vote.VoteRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventService {
    @GET("event")
    fun loadEvent(): Call<EventModel>

    // 방탈출

    @GET("room-escapes")
    fun loadQuestion(): Call<QuestionModel>

    @POST("room-escapes/answer")
    fun loadAnswer(
        @Body request: AnswerRequest
    ): Call<AnswerResponse>

    @GET("room-escapes/history")
    fun loadStatus(): Call<UserStatusModel>

    // 투표
    @GET("vote")
    fun loadVoteList(): Call<VoteModel>

    @GET("vote/{votingId}")
    fun loadDetailVote(
        @Path("votingId") votingId: Int
    ): Call<VoteModel>

    @POST("vote")
    fun postVote(
        @Body request: VoteRequest
    ): Call<VoteModel>
}