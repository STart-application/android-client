package com.start.STart.ui.home.rent

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.start.STart.R

enum class RentItem(
    val category: String,
    @DrawableRes val drawable: Int
) {
    // TODO: 대여 아이템 타입 수정
    MAT("돗자리", R.drawable.ic_mat),
    SIMPLE_TABLE("간이테이블", R.drawable.ic_simple_table),
    TABLE("듀라테이블", R.drawable.ic_table),
    AMP("앰프&마이크", R.drawable.ic_amp),
    CANOPY("캐노피", R.drawable.ic_canopy),
    WIRE("리드선", R.drawable.ic_wire),
    CART("L카", R.drawable.ic_cart),
    CHAIR("의자", R.drawable.ic_chair);
    companion object {
        const val KEY_RENT_ITEM_TYPE = "key_rent_item_type"
    }
}

enum class RentStatus(val description: String, @ColorRes val color: Int) {
    DENY("거절", R.color.dream_red),
    DONE("반납완료", R.color.text_caption),
    WAIT( "승인대기", R.color.dream_purple_ghost),
    CONFIRM("승인", R.color.dream_green),
    RENT("대여중", R.color.dream_purple);
}
