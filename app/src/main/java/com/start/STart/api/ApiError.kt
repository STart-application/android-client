package com.start.STart.api

enum class ApiError(val code: String, val message: String) {
    ST001("ST001", "잘못된 정보로 요청하였습니다."), // 값 검증 실패
    ST002("ST002", ""),
    ST010("ST010", ""),
    ST011("ST011", "토큰이 만료되었습니다."),
    ST030("ST030", "관리자 권한이 없습니다."),
    ST040("ST040", "지원하지 않는 경로입니다."),
    ST041("ST041", "찾을 수 없는 리소스입니다."),
    ST050("ST050", "(로그인) 비밀번호가 일치하지 않습니다."),
    ST051("ST051", ""),
    ST052("ST052", ""),
    ST053("ST053", "이미 같은 학번으로 가입된 계정이 존재합니다."),
    ST054("ST054", "(상시사업) 대여 물품의 개수가 유효하지 않습니다."),
    ST055("ST055", "(상시사업/어드민) ItemNo가 중복됩니다."),
    ST056("ST056", ""),
    ST057("ST057", "(로그인/회원가입) 학생증 인증이 되지 않은 회원입니다."),
    ST058("ST058", "(로그인/회원가입) 탈퇴한 회원입니다."),
    ST059("ST059", ""),
    ST060("ST060", "(스탬프) 중복 요청"),
    ST061("ST061", "(대여사업) 대여가 승인되지 않습니다."),
    ST062("ST062", "(스탬프) 모든 스탬프를 얻지 않았습니다."),
    ST063("ST063", "(?) 이미 상품을 수령하였습니다."),
    ST064("ST064", "(인증) 사용중인 전화번호 입니다."),
    ST065("ST065", "(인증) 인증 요청 횟수를 초과하였습니다.\n10분 후 다시 시도해 주세요."),
    ST066("ST066", "인증 정보가 일치하지 않   습니다."), // 휴대폰 인증
    ST067("ST067", "인증 시간이 만료되었습니다."), // 휴대폰 인증
    ST999("ST999", "서버 에러");

    companion object {
        fun getErrorMessage(code: String): String {
            return try {
                val enumConstant = ApiError.valueOf(code)
                enumConstant.message
            } catch (e: IllegalArgumentException) {
                "Unknown error code: $code"
            }
        }
    }
}