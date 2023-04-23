package com.start.STart.api.rent.response

data class GetMyRentResponse(
    val status: Int,
    val message: String,
    val data: List<MyRentData>,
)
