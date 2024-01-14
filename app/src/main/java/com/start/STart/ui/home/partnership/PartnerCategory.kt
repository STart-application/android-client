package com.start.STart.ui.home.partnership

import androidx.annotation.DrawableRes
import com.start.STart.R

enum class PartnerCategory(val id: Int, val title: String, @DrawableRes val marker: Int?, @DrawableRes val tag: Int?) {
    all(99, "전체보기", null, null),
    food(1, "음식점", R.drawable.ic_partner_food, R.drawable.ic_partner_category_food),
    cafe(2, "카페", R.drawable.ic_partner_cafe, R.drawable.ic_partner_category_cafe),
    pub(3, "주점", R.drawable.ic_partner_pub, R.drawable.ic_partner_category_pub),
    study(4, "학술", R.drawable.ic_partner_study, R.drawable.ic_partner_category_study),
    culture(5, "문화", R.drawable.ic_partner_culture, R.drawable.ic_partner_caterogy_culture),
    gym(6, "헬스장", R.drawable.ic_partner_gym, R.drawable.ic_partner_category_gym);

    companion object {
        fun findById(id: Int): PartnerCategory? {
            return values().find { it.id == id }
        }
    }
}