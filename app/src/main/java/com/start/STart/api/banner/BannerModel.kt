package com.start.STart.api.banner

import androidx.annotation.DrawableRes

data class BannerModel(
    val title: String,
    val imageUrl: String?,
    val priority: Int,
    val isDeleted: Boolean,

    @DrawableRes val imageDrawable: Int? = null
)