package com.start.STart.api.banner

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class EventModel(
    val status: Int,
    val message: String? = null,
    val data: List<Event>
)

@Parcelize
data class Event(
    val eventId: Int,
    val title: String,
    val formLink: String,
    val imageUrl: String,
    val startTime: String,
    val endTime: String,
    val eventStatus: String
): Parcelable
