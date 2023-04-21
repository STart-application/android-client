package com.start.STart.ui.home.festival.maps

import android.content.Context
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.start.STart.R
import com.start.STart.util.dp2px

data class MarkerModel(
    val context: Context,
    val name: String,
    val latLng: LatLng,
    @DrawableRes val drawableRes: Int,
): ClusterItem {
    lateinit var circleOptions: CircleOptions
    init {
        circleOptions = buildCircleOptions()
    }
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return null
    }


    private fun buildCircleOptions(): CircleOptions {
        return CircleOptions()
            .center(latLng)
            .radius(50.0)
            .clickable(true)
            .strokeWidth(context.dp2px(2f))
            .strokeColor(Color.parseColor("#979797"))
            .fillColor(Color.argb(51, 111 ,111, 111))
    }
}
