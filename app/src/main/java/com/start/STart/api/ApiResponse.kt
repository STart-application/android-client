package com.start.STart.api

data class ApiResponse(
    val status: Int,
    val message: String,
    val errorCode: String?,
    val data: List<*>?,
)