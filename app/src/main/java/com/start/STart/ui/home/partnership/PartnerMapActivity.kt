package com.start.STart.ui.home.partnership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.start.STart.R
import com.start.STart.api.partner.response.Partner
import com.start.STart.databinding.ActivityPartnerMapBinding
import com.start.STart.util.getParcelableExtra

class PartnerMapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val KEY_PARTNER = "key_partner"
    }

    private val binding by lazy { ActivityPartnerMapBinding.inflate(layoutInflater) }

    private lateinit var googleMap: GoogleMap

    private lateinit var partner: Partner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        partner = intent.getParcelableExtra<Partner>(key = KEY_PARTNER)!!

        (supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment)
            .getMapAsync(this)

        binding.btnBack.setOnClickListener { finish() }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if(partner.coordinateX != null && partner.coordinateY != null) {
            val latLng = LatLng(partner.coordinateX!!, partner.coordinateY!!)
            val position = CameraPosition.Builder()
                .target(latLng)
                .zoom(17f)
                .build()
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))

            val icon = IconGenerator(this).let {
                it.setColor(ContextCompat.getColor(this, R.color.dream_purple))
                it.setTextAppearance(this, R.style.Theme_Dream_Partner_Marker)
                it.makeIcon(partner.name)
            }


            googleMap.addMarker(
                MarkerOptions()
                .position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
            )
        }


    }
}