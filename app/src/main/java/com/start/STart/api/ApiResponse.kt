package com.start.STart.api

import com.start.STart.util.gson


data class ApiResponse(
    val status: Int,
    val message: String,
    val errorCode: String?,
    private val data: List<*>? = null,
) {
    fun getErrorMessage(): String {
        return "$errorCode: $message"
    }

    fun <T : Any> parseData(cls: Class<T>): T {
        return gson.fromJson(gson.toJson(data?.get(0)), cls)
    }
}