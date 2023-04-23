package com.start.STart.api.member.request

data class ResetPasswordWithLoginBody(
    var currentPassword: String,
    var newPassword: String,
)