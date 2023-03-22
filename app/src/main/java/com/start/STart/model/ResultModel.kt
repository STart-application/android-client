package com.start.STart.model

data class ResultModel(
    val isSuccessful: Boolean,
    val message: String? = null,
    val data: Any? = null
)