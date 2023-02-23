package com.start.STart.api.auth.request

data class VerifySmsCodeRequest(
    val phoneNo: String,
    val code: String,
)