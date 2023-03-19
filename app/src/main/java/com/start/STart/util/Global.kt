package com.start.STart.util

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.google.gson.Gson
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.start.STart.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

val gson = Gson()

fun TextView.setSuccessText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_purple))
}

fun TextView.setFailText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_red))
}

fun String?.toPlainRequestBody() =
    requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

inline fun <reified T : Parcelable> Intent.getParcelableExtra(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableExtra(key, T::class.java)
    } else {
        this.getParcelableExtra<T>(key)
    }
}

fun View.showRightBalloon(message: String?) {
    Balloon.Builder(this.context)
        .setText(message.toString())
        .setPadding(8)
        .setBackgroundColor(resources.getColor(R.color.dream_purple))
        .setArrowOrientation(ArrowOrientation.START)
        .setArrowPosition(0.5f)
        .build()
        .showAlignRight(this)
}

fun Context.openCustomTab(url: String) {
    CustomTabsIntent.Builder()
        .build()
        .launchUrl(this, Uri.parse(url))
}

fun Context.dp2px(dp: Float): Float {
    val resources: Resources = this.resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}