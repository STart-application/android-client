package com.start.STart.api.banner

data class BannerModel(
    val title: String,
    val imageUrl: String,
    val priority: Int,
    val isDeleted: Boolean,
)