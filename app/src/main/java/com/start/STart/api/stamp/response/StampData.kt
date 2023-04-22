package com.start.STart.api.stamp.response

data class StampData(
    val stampId: Int,
    val memberId: Int,
    val game: Boolean,
    val yard: Boolean,
    val stage: Boolean,
    val bungeobang: Boolean,
    val photo: Boolean,
)