package com.start.STart.api.banner

data class EventModel(
    val status: Int,
    val message: String? = null,
    val data: List<Event>
)

data class Event(
    val eventId: Int,
    val title: String,
    val formLink: String,
    val imageUrl: String,
    val startTime: String,
    val endTime: String,
    val eventStatus: String
)
