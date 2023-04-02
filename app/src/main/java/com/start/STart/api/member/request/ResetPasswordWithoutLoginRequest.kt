package com.start.STart.api.member.request

data class ResetPasswordWithoutLoginRequest(
    var password: String,
    var studentNo: String,
)