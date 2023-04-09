package com.start.STart.ui.home.festival

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.ActivityFestivalBinding
import com.start.STart.ui.home.festival.info.FestivalInfoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.start.STart.R
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
            if(!stampDialog.isAdded) {
                stampDialog.show(supportFragmentManager, ".StampDialog")
            }
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

        addMakers(listOf(
            MarkerModel("동심오락관", LatLng(37.6319, 127.079)),
            MarkerModel("마당사업", LatLng(37.632310, 127.077111)),
            MarkerModel("붕어방컨텐츠", LatLng(37.633061, 127.078598)),
            MarkerModel("무대", LatLng(37.6294, 127.0787)),
            MarkerModel("포토존", LatLng(37.633930, 127.077845)),
        ))

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))

    }

    private fun addMakers(markerModels: List<MarkerModel>) {
        markerModels.forEach {
            googleMap.addMarker(MarkerOptions()
                .position(it.latLng)
                .title(it.title)
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.marker_food_truck)))
            )
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context!!, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}