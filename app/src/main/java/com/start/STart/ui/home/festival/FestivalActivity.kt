package com.start.STart.ui.home.festival

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalBinding

class FestivalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityFestivalBinding.inflate(layoutInflater) }
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment)
            .getMapAsync(this)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val latLng = LatLng(37.6333, 127.0778)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .build()

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }
}