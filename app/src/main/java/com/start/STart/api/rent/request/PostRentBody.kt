package com.start.STart.api.rent.request

data class PostRentBody(
    val purpose: String,
    val account: Int,
    val itemCategory: String,
    val startTime: String,
    val endTime: String,
)