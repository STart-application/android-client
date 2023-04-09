package com.start.STart.ui.home.festival

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class MarkerModel(
    val name: String,
    val latLng: LatLng,
): ClusterItem {
    override fun getPosition(): LatLng {
        return latLng
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return "스니펫"
    }

}
