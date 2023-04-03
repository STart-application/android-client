package com.start.STart.ui.home.festival

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.skydoves.cloudy.Cloudy
import com.skydoves.cloudy.CloudyState
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalBinding
import com.start.STart.ui.home.festival.info.FestivalInfoActivity

class FestivalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityFestivalBinding.inflate(layoutInflater) }
    private lateinit var googleMap: GoogleMap

    private val stampDialog by lazy { StampStatusDialog()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        (supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment)
            .getMapAsync(this)

        initToolbar()
        initMenu()
        binding.composeView.setContent {
            Cloudy(radius = 10){

            }
        }
    }

    fun hideCompose() {
        binding.composeView.visibility = View.GONE
    }

    private fun initMenu() {
        binding.menu1.setOnClickListener {
            binding.composeView.visibility = View.VISIBLE
            stampDialog.show(supportFragmentManager, ".FestivalActivity")
        }
        binding.menu2.setOnClickListener {
            startActivity(Intent(this, FestivalInfoActivity::class.java))
        }
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "어의대동제"
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