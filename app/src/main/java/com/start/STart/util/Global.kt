package com.start.STart.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.google.gson.Gson
import com.start.STart.BuildConfig
import com.start.STart.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

fun uriToFile(context: Context, uri: Uri): File {
    //val name = getFileNameFromUri(context, uri!!)

    val fileName = when(context.contentResolver.getType(uri)) {
        //"image/png" -> { "cache_image.png" }
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
        val file = uriToFile(context, uri).also { file ->
            compressImage(file.absolutePath, 0.3)
        }
        val requestFile = file.asRequestBody(context.contentResolver.getType(uri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
    return null
}

fun compressImage(filePath: String, targetMB: Double = 1.0) {
    var image: Bitmap = BitmapFactory.decodeFile(filePath)

    val exif = ExifInterface(filePath)
    val exifOrientation: Int = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
    )

    val exifDegree: Int = exifOrientationToDegrees(exifOrientation)

    image = rotateImage(image, exifDegree.toFloat())

    try {

        val fileSizeInMB = getFileSizeInMB(filePath)

        var quality = 100
        if(fileSizeInMB > targetMB) {
            quality = ((targetMB / fileSizeInMB) * 100).toInt()
        }

        val fileOutputStream  = FileOutputStream(filePath)
        image.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
        fileOutputStream.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun getFileSizeInMB(filePath: String): Double {
    val file = File(filePath)
    val length = file.length()

    val fileSizeInKB = (length / 1024).toString().toDouble()
    val fileSizeInMB = (fileSizeInKB / 1024).toString().toDouble()

    return fileSizeInMB
}

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)

    return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
}

fun exifOrientationToDegrees(exifOrientation: Int): Int {
    return when (exifOrientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            90
        }

        ExifInterface.ORIENTATION_ROTATE_180 -> {
            180
        }

        ExifInterface.ORIENTATION_ROTATE_270 -> {
            270
        }

        else -> 0
    }
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

fun TextView.setSuccessText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_purple))
}

fun TextView.setFailText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_red))
}


fun Context.openPdf(fileName: String) {
    val uri = assetToUri(this, fileName) // Assets 폴더에 있는 PDF 파일을 내부 저장소로 복사

    val intent = Intent(Intent.ACTION_VIEW).also {
        it.setDataAndType(uri, "application/pdf")
        it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    try {
        startActivity(intent)
    } catch (e: Exception) { // 오류 상세 파악
        e.printStackTrace()
        showErrorToast(this)
    }
}

fun assetToUri(context: Context, fileName: String): Uri? {
    val file = File("${context.filesDir}/$fileName")
    try {
        if (!file.exists()) { // 파일이 이미 존재할 경우 복사하지 않음
            val outputStream = FileOutputStream(file)
            context.resources.assets.open(fileName).use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }
        return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
    } catch (e: IOException) {
        e.printStackTrace()
        file.delete()
        showErrorToast(context)
    }
    return null
}