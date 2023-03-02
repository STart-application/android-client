package com.start.STart.util

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.widget.TextView
import com.start.STart.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

fun TextView.setSuccessText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_purple))
}

fun TextView.setFailText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_red))
}

fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

inline fun <reified T: Parcelable> Intent.getParcelableExtra(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(key, T::class.java)
    } else {
        this.getParcelableExtra<T>(key)
    }
}