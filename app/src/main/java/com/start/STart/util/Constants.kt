package com.start.STart.util

import java.net.SocketTimeoutException

object Constants {
    // Flags
    const val SIGN_IN = "no_sign_in"

    // Extra Data
    const val KEY_REGISTER_DATA = "key_register_data"
    const val KEY_STUDENT_ID = "key_student_id"

    // EncryptedSharedPreference
    const val KEY_ENCRYPTED_PREFERENCE = "key_encrypted_preference"
    const val KEY_ACCESS_TOKEN = "key_access_token"
    const val KEY_REFRESH_TOKEN = "key_refresh_token"
    const val KEY_MEMBER_DATA = "key_member_data"

    // API: *변경 금지*
    const val KEY_API_STUDENT_NO = "studentNo"
    const val KEY_API_PASSWORD = "appPassword"
    const val KEY_API_NAME = "name"
    const val KEY_API_DEPARTMENT = "department"
    const val KEY_API_FCM_TOKEN = "fcmToken"
    const val KEY_API_PHONE_NO = "phoneNo"

    const val KEY_AUTHORIZATION = "Authorization"

    // 예외 처리
    const val EXCEPTION_TIME_OUT = "timeout"
    const val EXCEPTION_CONNECTION = "connection"
    const val EXCEPTION_IOEXCEPTION = "io_exception"
    const val EXCEPTION_UNEXPECTED = "unexpected"
}