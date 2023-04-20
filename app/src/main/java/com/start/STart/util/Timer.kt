package com.start.STart.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val TIMER_ENDED = -1L

fun timerFlow(duration: Int = 3 * 60 * 1000, period: Long = 50): Flow<Long> = flow {
    val startTime = System.currentTimeMillis()
    while (true) {
        val elapsedTime = System.currentTimeMillis() - startTime
        if (elapsedTime >= duration) {
            break
        } else {
            emit(duration - elapsedTime)
        }
        delay(period)
    }
}


fun formatTimerMilliseconds(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}