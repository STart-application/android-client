package com.start.STart.ui.home.festival

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        showIntroDialog()
    }

    private fun showIntroDialog() = lifecycleScope.launch(Dispatchers.Default) {
        delay(500)
        withContext(Dispatchers.Main) {
            FestivalIntroDialog().show(supportFragmentManager, ".FestivalInfoDialog")
        }
    }
    
    fun showBlur() {
        binding.composeView.setContent {
            var radius by remember { mutableStateOf(0) }
            LaunchedEffect(Unit) {
                radius = 20
                (supportFragmentManager.findFragmentByTag(".FestivalInfoDialog") as FestivalIntroDialog?)?.load()
                (supportFragmentManager.findFragmentByTag(".StampDialog") as StampStatusDialog?)?.load()
                binding.composeView.visibility = View.VISIBLE
            }
            Cloudy(radius = radius) {

            }
        }

    }

    fun hideCompose() {
        binding.composeView.visibility = View.GONE
        binding.composeView.setContent{ }
    }

    private fun initMenu() {
        binding.menu1.setOnClickListener {
            stampDialog.show(supportFragmentManager, ".StampDialog")
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