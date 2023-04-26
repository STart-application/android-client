package com.start.STart.ui.auth.util

import com.start.STart.api.member.response.MemberData
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager

object AuthenticationUtil {
    val loginFailMessage = "로그인이 필요합니다!"

    private val isUserLoggedIn
        get() = !PreferenceManager.getBoolean(Constants.PREF_FLAG_WITHOUT_LOGIN)

    val loggedInMemberData
        get() = PreferenceManager.loadFromPreferences<MemberData>(Constants.PREF_KEY_MEMBER_DATA)

    fun performActionOnLogin(successListener: (AuthenticationUtil) -> Unit, failListener: ((AuthenticationUtil) -> Unit)? = null) {
        if (isUserLoggedIn) { successListener(this) }
        else { failListener?.invoke(this) }
    }
}