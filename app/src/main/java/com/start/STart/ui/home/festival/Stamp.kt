package com.start.STart.ui.home.festival

import androidx.annotation.DrawableRes
import com.start.STart.R

enum class StampEnum(@DrawableRes val drawable: Int, @DrawableRes val drawable_e: Int) {
    bungeobang(R.drawable.stamp_bungeobang, R.drawable.stamp_bungeobang_e),
    photo(R.drawable.stamp_photo, R.drawable.stamp_photo_e),
    game(R.drawable.stamp_game, R.drawable.stamp_game_e),
    yard(R.drawable.stamp_yard, R.drawable.stamp_yard_e),
    stage(R.drawable.stamp_stage, R.drawable.stamp_stage_e),
}