package com.start.STart.util

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.start.STart.R
import es.dmoral.toasty.Toasty

fun showSuccessToast(context: Context, message: String) {
    Toasty.custom(
        context,
        message,
        ContextCompat.getDrawable(context, es.dmoral.toasty.R.drawable.ic_check_white_24dp),
        ContextCompat.getColor(context, R.color.dream_green),
        ContextCompat.getColor(context, R.color.white),
        Toast.LENGTH_LONG,
        true,
        true
    ).show()
}

fun showErrorToast(context: Context, message: String) {
    Toasty.custom(
        context,
        message,
        ContextCompat.getDrawable(context, es.dmoral.toasty.R.drawable.ic_clear_white_24dp),
        ContextCompat.getColor(context, R.color.dream_green),
        ContextCompat.getColor(context, R.color.white),
        Toast.LENGTH_LONG,
        true,
        true
    ).show()
}