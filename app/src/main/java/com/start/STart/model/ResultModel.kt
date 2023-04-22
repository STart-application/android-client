package com.start.STart.model

import com.start.STart.util.AppException

data class ResultModel(
    val isSuccessful: Boolean,
    val message: String? = null,
    val subMessage: String? = null,
    val exception: AppException? = null,
    val data: Any? = null,
    val errorCode: String? = null,
)