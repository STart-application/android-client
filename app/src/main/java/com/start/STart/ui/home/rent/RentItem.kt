package com.start.STart.ui.home.rent

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.start.STart.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RentItem(
    val category: String,
    @DrawableRes val iconDrawable: Int,
    @DrawableRes val realDrawable: Int,
    val purpose: String,
    val caution: String,
): Parcelable {

    MAT("돗자리", R.drawable.rent_mat_icon, R.drawable.rent_mat, "붕어방에서 피크닉 할 때 사용할 수 있습니다.", "1. 찢어지지 않게 사용해주세요.\n\n2. 깨끗하게 사용해주세요."),
    S_TABLE("간이테이블", R.drawable.rent_s_table_icon, R.drawable.rent_s_table, "간이 테이블로 사용할 수 있습니다.", "1. 깨끗하게 사용해주세요."),
    TABLE("듀라테이블", R.drawable.rent_table_icon,  R.drawable.rent_table, "테이블로 사용할 수 있습니다.", "1. 듀라테이블을 조립하거나 정리할 때, 테이블 다리 접합부 또는 관절 부분에 손이 끼이지 않도록 주의해 주세요."),
    AMP("앰프&마이크", R.drawable.rent_amp_icon, R.drawable.rent_amp, "행사 시에 큰 음향을 낼 수 있습니다.", "1. 앰프에 물이 들어가지 않도록 해야 합니다.\n\n2. 다른 장비를 연결한 뒤에 앰프의 전원을 켜주세요.\n\n3. 사용 후에는 볼륨노브를 0으로 설정한 뒤 앰프의 전원을 끄고 장비를 분리해 주세요."),
    CANOPY("캐노피", R.drawable.rent_canopy_icon, R.drawable.rent_canopy, "기둥과 천막으로 부스를 만들 수 있습니다.", "1. 캐노피를 설치하거나 기둥 높이를 조절 할 때에는 여럿이서 작업해야 합니다.\n\n2. 운반 시에 끌지 않고 들어서 이동시켜야 합니다.\n\n3. 캐노피를 경사면에 설치하지 않도록 해주세요."),
    WIRE("리드선", R.drawable.rent_wire_icon, R.drawable.rent_wire, "콘센트를 연장하여 사용할 수 있습니다.", "1. 선을 말아서 정리할 때, 릴의 한쪽으로 선이 치우치지 않도록 주의해주세요.\n\n2. 리드선을 모두 풀어서 사용해야 부하를 최소화할 수 있습니다."),
    CART("엘카", R.drawable.rent_cart_icon, R.drawable.rent_cart, "여러 짐을 한 번에 옮길 수 있습니다.", "1. 바퀴가 고장나지 않도록 조심히 다뤄주세요.\n\n2. 카트를 끌 때, 사람이 올라 타지 않도록 해야 합니다."),
    CHAIR("의자", R.drawable.rent_chair_icon, R.drawable.rent_chair, "외부 행사 시에 간이 의자를 활용할 수 있습니다.", "1. 의자를 포개서 정리할 때, 의자 사이에 손이 끼이지 않도록 주의해 주세요.\n\n2. 의자 위에 무거운 물건을 올리지 말아주세요.");
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
