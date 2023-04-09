package com.start.STart.api.member.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterData(
    var studentNo: String? = null,
    var appPassword: String? = null,
    var name: String? = null,
    var department: String? = null,
    var phoneNo: String? = null,
    var fcmToken: String = "none", // 초기 버전에서 FCM 사용하려다가 안한듯

    // 내부 사용
    var college: String? = null
): Parcelable
