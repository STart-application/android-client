package com.start.STart.api.auth.request

data class VerifyResetPasswordCode(
    val studentNo: String,
    val code: String,
)
