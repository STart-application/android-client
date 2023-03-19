package com.start.STart.api.auth.response

data class TokenData(
    val accessToken: String? = null,
    val refreshToken: String? = null,
)