package com.start.STart.api.banner

data class VoteModel(
    val status: Int,
    val message: String? = null,
    val data: List<Vote>
)

data class Vote(
    val votingId: Int,
    val title: String,
    val status: String,
    val description: String,
    val minSelect: Int,
    val maxSelect: Int,
    val displayStartDate: String,
    val displayEndDate: String,
    val voteOptionList: List<VoteOption>
)

data class VoteOption(
    val votingOptionId: Int,
    val optionTitle: String,
    val status: String
)
