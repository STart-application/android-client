package com.start.STart.ui.home.festival.maps

import android.content.Context
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.start.STart.ui.home.festival.StampData
import com.start.STart.util.dp2px

data class MarkerModel(
    val context: Context,
    val stampData: StampData
): ClusterItem {
    lateinit var circleOptions: CircleOptions
    lateinit var circle: Circle

    override fun getPosition(): LatLng {
        return stampData.latLng
    }

    override fun getTitle(): String? {
        return stampData.title
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return null
    }


    fun buildCircle(googleMap: GoogleMap) {
        circleOptions = CircleOptions()
            .center(stampData.latLng)
            .radius(50.0)
            .clickable(true)
            .strokeWidth(context.dp2px(2f))
            .strokeColor(Color.parseColor("#979797"))
            .fillColor(Color.argb(51, 111 ,111, 111))

        circle = googleMap.addCircle(circleOptions).apply {
            isVisible = false
        }
    }
}
