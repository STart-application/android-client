package com.start.STart.api.event.vote

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
    val voteOptionList: List<VoteOption>,
    val userSelectedOptionIds: List<Int>
)

data class VoteOption(
    val votingOptionId: Int,
    val optionTitle: String,
    val status: String
)

data class VoteRequest(
    var votingId: Int,
    var votingOptionIds: MutableList<Int>
)
