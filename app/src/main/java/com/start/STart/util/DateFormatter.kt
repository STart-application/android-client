package com.start.STart.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun format(any: Any): String {
        return format.format(any)
    }

    fun parse(s: String): Date {
        return format.parse(s)
    }
}