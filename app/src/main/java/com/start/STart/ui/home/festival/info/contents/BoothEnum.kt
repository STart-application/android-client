package com.start.STart.ui.home.festival.info.contents

import androidx.annotation.DrawableRes
import com.start.STart.R

enum class BoothEnum(var boothName: String, val locationNPeriod: String, @DrawableRes val drawableRes: Int, val description: String, var congestion: Int = 1) {
    Booth1("운영부스 1", "향학로 입구\n10:00 - 17:00", R.drawable.booth_operation, "어의대동제 행사 진행 관련 개인정보 수집 동의 구글폼 작성\n\n자치회비 납부 여부를 확인하고 팔찌 배부를 하는 부스"),
    Booth2("운영부스 2", "붕어방\n10:00 - 16:30", R.drawable.booth_operation,"어의대동제 행사 진행 관련 개인정보 수집 동의 구글폼 작성\n\n자치회비 납부 여부를 확인하고 팔찌 배부를 하는 부스"),
    Booth3("운영부스 3", "운동장\n17:00 -", R.drawable.booth_operation,"어의대동제 행사 진행 관련 개인정보 수집 동의 구글폼 작성\n\n자치회비 납부 여부를 확인하고 팔찌 배부를 하는 부스"),
    Booth4("붕어방의 산신령", "붕어방\n10:00 - 18:00", R.drawable.booth_4,"돗자리(쇠도끼)를 빌려 즐기는 피크닉 부스\n\n(미니게임 : 퍼즐 타임어택)"),
    Booth5("임금님 귀는 당나귀 귀", "붕어방\n10:00 - 18:00", R.drawable.booth_operation,"총학생회와의 소통을 위한 부스\n\n자신이 마음에 담아두고 있던 말이나 하고 싶었던 이야기들을 적고 머리핀 받아가세요!"),
    Booth6("릴레이 소설", "붕어방\n10:00 - 18:00", R.drawable.booth_6,"3일 동안 주어지는 주제로 함께 만들어가는 릴레이 소설 제작을 위한 부스\n\n참여 시 추첨을 통해 상품권을 드립니다!"),
    Booth7("꿈에서 핀 오작교", "붕어방\n10:00 - 18:00", R.drawable.booth_7,"우리학교의 상징 붕어방에서 만남을 이루어 보아요!\n\n이번 오작교에서의 만남을 통해 이상형과 함께하는 축제가 되어보는 건 어떨까요\n\n본인 소개를 작성하신 뒤 마음에 드는 이상형이 있는 별사탕을 가져가세요!"),
    Booth8("동심 오락실", "창학관 앞\n10:00 - 18:00", R.drawable.booth_8,"오락 게임존에서 어릴적 추억을 회상하세요!\n\n(투호, 공기놀이, 제기차기, 장기, 오목, 구슬미로, 컵스택 등)"),
    Booth9("마당사업", "향학로\n10:00 - 18:00", R.drawable.booth_9,"향학로 부스에서 여러 가지 콘텐츠 기획 & 운영\n\n- 후크 선장의 키링 공방\n\n- 보물 찾기\n\n- 후크선장에게서 탈출하라!(미니 4종 게임)\n\n- 후크선장의 물고기 사냥터"),
}