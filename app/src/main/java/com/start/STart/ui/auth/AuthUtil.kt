package com.start.STart.ui.auth.util

import android.widget.TextView
import com.start.STart.R

fun TextView.setSuccessText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_purple))
}

fun TextView.setFailText(text: String) {
    this.text = text
    this.setTextColor(resources.getColor(R.color.dream_red))
}
