package com.start.STart.api

data class ApiResponse(
    val status: Int,
    val message: String,
    val data: List<*>,
)