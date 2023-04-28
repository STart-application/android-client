package com.start.STart.api.banner

// 8문제 조회
data class QuestionModel(
    val status: Int,
    val message: String,
    val data: List<Question>
)

data class Question(
    val roomId: Int,
    val imageUrl: String
)

// 유저 문제 정보
data class UserStatusModel(
    val status: Int,
    val message: String,
    val data: List<UserStatus>
)

data class UserStatus(
    val roomId: Int
)


// 정답 제출
data class AnswerRequest(
    val roomId: Int,
    val answer: String
)

data class AnswerResponse(
    val status: Int,
    val message: String,
    val data: List<AnswerBody>
)

data class AnswerBody(
    val answer: Boolean
)