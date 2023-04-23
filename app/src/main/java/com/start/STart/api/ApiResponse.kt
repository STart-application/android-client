package com.start.STart.api

import android.util.Log
import com.start.STart.util.gson


data class ApiResponse(
    val status: Int,
    val message: String,
    val errorCode: String?,
    val data: List<*>? = null,
) {

    fun <T : Any> parseData(cls: Class<T>): T {
        return gson.fromJson(gson.toJson(data?.get(0)), cls)
    }

    fun <T : Any> parseDataList(cls: Class<T>): List<T> {
        val list = data?.map {
            gson.fromJson(gson.toJson(it), cls)
        } as List<T>
        Log.d(this::class.java.simpleName, "parseDataList: ${list.joinToString(" ")}")
        return list
    }
}