package com.start.STart.api.rent.response

data class RentData(
    val rentId: Int,
    val renter: String?,
    val account: Int,
    val purpose: String,
    val rentStatus: String,
    val itemCategory: String,
    val startTime: String,
    val endTime: String,
    val rentItemList: String?, // TODO: 이건 뭘까?
    val createdAt: String,
    val updatedAt: String,
)