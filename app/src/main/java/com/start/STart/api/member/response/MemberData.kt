package com.start.STart.api.member.response

data class MemberData(
    val studentNo: String,
    val name: String,
    val department: String,
    val phoneNo: String,
    val memberShip: Boolean,
    val memberStatus: String,
)
