package com.start.STart.api.rent.response

data class MyRentData(
    val rentId: Int,
    val account: Int,
    val purpose: String,
    val rentStatus: String,
    val itemCategory: String,
    val startTime: String,
    val endTime: String,
    val createdAt: String,
    val updatedAt: String,
)