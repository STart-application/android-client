package com.start.STart.ui.home.rent

import androidx.annotation.DrawableRes

data class RentItem(
    val type: String,
    val name: String,
    @DrawableRes val drawable: Int
)