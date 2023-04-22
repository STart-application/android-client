package com.start.STart.ui.home.festival

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng
import com.start.STart.R

enum class StampData(val title: String, @DrawableRes val drawable: Int, @DrawableRes val drawable_e: Int, @DrawableRes val marker: Int, val latLng: LatLng) {
    bungeobang("붕어방", R.drawable.stamp_bungeobang, R.drawable.stamp_bungeobang_e, R.drawable.marker_bungeobang, LatLng(37.633061, 127.078598)),
    photo("포토존", R.drawable.stamp_photo, R.drawable.stamp_photo_e, R.drawable.marker_photo, LatLng(37.633930, 127.077845)),
    game("동심오락관", R.drawable.stamp_game, R.drawable.stamp_game_e, R.drawable.marker_game, LatLng(37.6319, 127.079)),
    yard("마당사업", R.drawable.stamp_yard, R.drawable.stamp_yard_e, R.drawable.marker_yard, LatLng(37.632310, 127.077111)),
    stage("무대", R.drawable.stamp_stage, R.drawable.stamp_stage_e, R.drawable.marker_stage, LatLng(37.6294, 127.0787)),
}