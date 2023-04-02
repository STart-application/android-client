package com.start.STart.ui.home.rent

import androidx.annotation.DrawableRes
import com.start.STart.R

data class RentItem(
    val type: String,
    val name: String,
    @DrawableRes val drawable: Int
) {
    companion object {
        const val KEY_RENT_ITEM_TYPE = "key_rent_item_type"

        const val CANOPY = "CANOPY"
        const val TABLE = "TABLE"
        const val AMP = "AMP"
        const val WIRE = "WIRE"
        const val CART = "CART"
        const val CHAIR = "CHAIR"
        const val MAT = "돗자리"
    }
}