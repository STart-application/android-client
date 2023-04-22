package com.start.STart.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.google.gson.Gson
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.start.STart.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


val gson = Gson()

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

fun View.showTopBalloon(message: String?) {
    Balloon.Builder(this.context)
        .setText(message.toString())
        .setPadding(8)
        .setBackgroundColor(resources.getColor(R.color.dream_purple))
        .build()
        .showAlignTop(this)
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

fun Context.px2dp(px: Float): Float {
    val resources: Resources = this.resources
    val metrics: DisplayMetrics = resources.displayMetrics
    return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}


fun <T : Any> T.toHashMap(): HashMap<String, Any?> {
    val hashMap = HashMap<String, Any?>()
    val properties = this::class.memberProperties
    for (property in properties) {
        property.isAccessible = true
        hashMap[property.name] = property.getter.call(this)
    }
    return hashMap
}

fun uriToFile(context: Context, uri: Uri): File {
    //val name = getFileNameFromUri(context, uri!!)

    val fileName = when(context.contentResolver.getType(uri)) {
        "image/png" -> { "cache_image.png" }
        else -> { "cache_image.jpg" }
    }

    val file = File(context.externalCacheDir, fileName)

    uri.let {
        context.contentResolver.openInputStream(it)?.let { inputStream ->
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
    return file
}

fun getPart(context: Context, uri: Uri?): MultipartBody.Part? {
    uri?.let {
        val file = uriToFile(context, uri)
        val requestFile = file.asRequestBody(context.contentResolver.getType(uri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
    return null
}

@SuppressLint("Range")
fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var fileName: String? = null
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName
}

fun View.contains(x: Int, y: Int): Boolean {
    val l = this.left
    val t = this.top
    val r = this.right
    val b = this.bottom
    return x in l..r && y in t..b
}





