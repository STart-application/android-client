package com.start.STart.ui.home.event

import androidx.annotation.ColorRes
import com.start.STart.R

enum class EventStatus(@ColorRes val titleColor: Int, val buttonEnabled: Boolean) {
    PROCEEDING(R.color.dream_purple, true),
    BEFORE(R.color.dream_purple_ghost, false),
    END(R.color.text_caption, false);
}