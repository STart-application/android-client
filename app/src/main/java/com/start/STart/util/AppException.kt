package com.start.STart.util

enum class AppException(val title: String, val sub: String,) {
    TIMEOUT("인터넷 연결 오류", "인터넷 연결을 확인해주세요."),
    CONNECTION("네트워크 연결 지연", "잠시 후 다시 시도해주세요."),
    UNEXPECTED("예기치 못한 오류 발생", "잠시 후 다시 시도해주세요."),
}