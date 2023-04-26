package com.start.STart.ui.home.festival

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalBinding
import com.start.STart.ui.auth.util.AuthenticationUtil
import com.start.STart.ui.home.festival.dialogs.FestivalIntroDialog
import com.start.STart.ui.home.festival.dialogs.FoodTruckDialog
import com.start.STart.ui.home.festival.dialogs.PhotoZoneDialog
import com.start.STart.ui.home.festival.dialogs.PostStampDialog
import com.start.STart.ui.home.festival.dialogs.StampStatusDialog
import com.start.STart.ui.home.festival.info.FestivalInfoActivity
import com.start.STart.ui.home.festival.maps.MarkerModel
import com.start.STart.util.PermissionHelper
import com.start.STart.util.getBitmapFromVectorDrawable
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FestivalActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityFestivalBinding.inflate(layoutInflater) }

    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private val permissionHelper = PermissionHelper(this)
    private val locationPermissions = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MarkerModel>

    private val markerList = mutableListOf<MarkerModel>()

    private val stampDialog by lazy { StampStatusDialog() }
    private val photoZoneDialog by lazy { PhotoZoneDialog() }
    private val foodTruckDialog by lazy { FoodTruckDialog() }
    private val postStampDialog by lazy { PostStampDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        (supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment)
            .getMapAsync(this)

        initToolbar()
        initMenu()

        showIntroDialog()
    }

    private fun showIntroDialog() = lifecycleScope.launch(Dispatchers.Default) {
        delay(500)
        withContext(Dispatchers.Main) {
            FestivalIntroDialog().show(supportFragmentManager, null)
        }
    }

    private fun initMenu() {
        binding.menu1.setOnClickListener {
            startActivity(Intent(this, FestivalInfoActivity::class.java))
        }

        binding.menu2.setOnClickListener {
            AuthenticationUtil.performActionOnLogin({
                if(!stampDialog.isAdded) stampDialog.show(supportFragmentManager, ".StampDialog")
            }, failListener = {
                Toasty.info(this, "로그인이 필요한 기능입니다.").show()
            })
        }

        binding.menu3.setOnClickListener {
            if(!foodTruckDialog.isAdded) {
                foodTruckDialog.show(supportFragmentManager, ".FoodTruckDialog")
            }
        }
        binding.menu4.setOnClickListener {
            if(!photoZoneDialog.isAdded) {
                photoZoneDialog.show(supportFragmentManager, ".PhotoZoneDialog")
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "어의대동제"
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings
            .isMyLocationButtonEnabled = false

        initPosition()


        initMarker()
        setUpClusterManager()


        binding.locationBtn.setOnClickListener {
            permissionHelper.checkAndRequestPermissions(locationPermissions,
                grantedCallback = {
                    setCurrentPosition()
                }
            )
        }
    }

    private fun initPosition() {
        val latLng = LatLng(37.6333, 127.0778)
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(17f)
            .build()
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    private fun initMarker() {

        addMakers()

        googleMap.setOnCircleClickListener { circle ->
            if(circle.isVisible) {
                permissionHelper.checkAndRequestPermissions(locationPermissions,
                    grantedCallback = {
                        openStampDialog(markerList.first { it.circle == circle }.stampData)
                    }
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentPosition() {
        googleMap.isMyLocationEnabled = true
        fusedLocationProvider.lastLocation.addOnSuccessListener {
            val latLng = LatLng(it.latitude, it.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
            googleMap.animateCamera(cameraUpdate)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getMyLocation()  = suspendCoroutine { contination ->
        fusedLocationProvider.lastLocation.addOnSuccessListener {
            contination.resume(LatLng(it.latitude, it.longitude))
        }
    }

    private fun setUpClusterManager() {
        clusterManager = ClusterManager<MarkerModel>(this, googleMap).apply {
            addItems(markerList)
        }

        clusterManager.renderer = object: DefaultClusterRenderer<MarkerModel>(this@FestivalActivity, googleMap, clusterManager
        ) {
            init {
                minClusterSize = StampData.values().size
            }

            override fun onBeforeClusterRendered(
                cluster: Cluster<MarkerModel>,
                markerOptions: MarkerOptions
            ) {
                super.onBeforeClusterRendered(cluster, markerOptions)

                markerList.forEach {
                    it.circle.isVisible = false
                }
            }

            override fun getColor(clusterSize: Int): Int {
                return Color.parseColor("#502182")
            }

            override fun onBeforeClusterItemRendered(
                item: MarkerModel,
                markerOptions: MarkerOptions
            ) {
                super.onBeforeClusterItemRendered(item, markerOptions)
                markerOptions
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this@FestivalActivity, item.stampData.marker)))

                item.circle.isVisible = true
            }

            override fun setOnClusterItemClickListener(listener: ClusterManager.OnClusterItemClickListener<MarkerModel>?) {
                super.setOnClusterItemClickListener(listener)
            }
        }

        clusterManager.setOnClusterItemClickListener {
            permissionHelper.checkAndRequestPermissions(locationPermissions,
                grantedCallback = {
                    openStampDialog(it.stampData)
                }
            )
            true
        }
        googleMap.setOnCameraIdleListener(clusterManager)
    }

    private fun addMakers() {

        StampData.values().forEach {
            val markerModel = MarkerModel(this, it).apply {
                buildCircle(googleMap)
            }

            markerList.add(markerModel)
        }
    }

    private fun moveCamera(latLng: LatLng, callback: GoogleMap.CancelableCallback) {
        val cameraUpdate = CameraUpdateFactory.newLatLng(latLng)
        googleMap.animateCamera(cameraUpdate, 300, callback)
    }

    private fun openStampDialog(stampData: StampData) {
        moveCamera(stampData.latLng, object: GoogleMap.CancelableCallback {
            override fun onCancel() {  }

            override fun onFinish() {
                AuthenticationUtil.performActionOnLogin({
                    if(!postStampDialog.isAdded) {
                        postStampDialog.setData(stampData)
                        postStampDialog.show(supportFragmentManager, ".PostStampDialog")
                    }
                }, failListener = {
                    Toasty.info(this@FestivalActivity, "로그인이 필요한 기능입니다.").show()
                })
            }
        })
    }
}