package com.start.STart.model

import com.start.STart.util.AppException
import java.io.IOException

data class ResultModel(
    val isSuccessful: Boolean,
    val message: String? = null,
    val subMessage: String? = null,
    val exception: AppException? = null,
    val data: Any? = null,
)