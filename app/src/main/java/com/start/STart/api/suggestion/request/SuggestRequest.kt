package com.start.STart.api.suggestion.request

import okhttp3.MultipartBody

data class SuggestRequest(
    val title: String,
    val content: String,
    val category: String,
    val file: MultipartBody.Part?,
)
