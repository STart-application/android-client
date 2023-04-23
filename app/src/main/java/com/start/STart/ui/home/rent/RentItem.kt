package com.start.STart.ui.home.rent

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.start.STart.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RentItem(
    val category: String,
    @DrawableRes val drawable: Int,
    val purpose: String,
    val caution: String,
): Parcelable {
    // TODO: 대여 아이템 타입 수정
    MAT("돗자리", R.drawable.ic_mat, "붕짜 가능", "집에 들고 가지 마세요."),
    S_TABLE("간이테이블", R.drawable.ic_simple_table, "도박", "집에 들고 가지 마세요."),
    TABLE("듀라테이블", R.drawable.ic_table, "화투", "집에 들고 가지 마세요."),
    AMP("앰프&마이크", R.drawable.ic_amp, "노래", "집에 들고 가지 마세요."),
    CANOPY("캐노피", R.drawable.ic_canopy, "수면", "집에 들고 가지 마세요."),
    WIRE("리드선", R.drawable.ic_wire, "연장", "집에 들고 가지 마세요."),
    CART("L카", R.drawable.ic_cart, "카트", "집에 들고 가지 마세요."),
    CHAIR("의자", R.drawable.ic_chair, "몰라", "집에 들고 가지 마세요.");
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
