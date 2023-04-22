package com.start.STart.api.festival.response

data class LoadBoothResponse(
    val status: Int,
    val message: String,
    val data: List<FestivalInfoData>,
)
