package com.start.STart.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.start.STart.R
import es.dmoral.toasty.Toasty

fun showSuccessToast(context: Context, message: String?) {
    Toasty.custom(
        context,
        message?:"요청이 성공적으로 처리되었습니다.",
        ContextCompat.getDrawable(context, es.dmoral.toasty.R.drawable.ic_check_white_24dp),
        ContextCompat.getColor(context, R.color.dream_green),
        ContextCompat.getColor(context, R.color.white),
        Toast.LENGTH_LONG,
        true,
        true
    ).show()
}

fun showErrorToast(context: Context, message: String? = null) {
    Toasty.custom(
        context,
        message?:"잠시 후 다시 시도하여 주세요.",
        ContextCompat.getDrawable(context, es.dmoral.toasty.R.drawable.ic_clear_white_24dp),
        ContextCompat.getColor(context, R.color.dream_red),
        ContextCompat.getColor(context, R.color.white),
        Toast.LENGTH_LONG,
        true,
        true
    ).show()
}