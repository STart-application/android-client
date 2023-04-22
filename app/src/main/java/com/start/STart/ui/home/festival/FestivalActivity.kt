package com.start.STart.ui.home.festival

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
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
import com.skydoves.cloudy.Cloudy
import com.start.STart.R
import com.start.STart.databinding.ActivityFestivalBinding
import com.start.STart.ui.home.festival.info.FestivalInfoActivity
import com.start.STart.ui.home.festival.maps.MarkerModel
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

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private lateinit var fusedLocationProvider: FusedLocationProviderClient

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        if(result.all { it.value }) {
            // 권한 승인
        } else {
            Toasty.info(this, "위치권한을 거부하였습니다.").show()
        }
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var clusterManager: ClusterManager<MarkerModel>

    private val markerList = mutableListOf<MarkerModel>()

    private val stampDialog by lazy { StampStatusDialog()}
    private val photoZoneDialog by lazy { PhotoZoneDialog()}
    private val foodTruckDialog by lazy { FoodTruckDialog()}
    private val postStampDialog by lazy { PostStampDialog()}

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
            startActivity(Intent(this, FestivalInfoActivity::class.java))
        }
        binding.menu2.setOnClickListener {
            if(!stampDialog.isAdded) {
                stampDialog.show(supportFragmentManager, ".StampDialog")
            }
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
            enableLocation()
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
                lifecycleScope.launch {
                    val myLatLng = withContext(Dispatchers.IO) { getMyLocation() }
                    val stampData = markerList.first { it.circle == circle }.stampData

                    openStampDialog(stampData)
                }
            }
        }
    }

    private fun enableLocation() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            fusedLocationProvider.lastLocation.addOnSuccessListener {
                val latLng = LatLng(it.latitude, it.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                googleMap.animateCamera(cameraUpdate)
            }
            return
        }
        permissionLauncher.launch(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))
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
            lifecycleScope.launch {
                val myLatLng = withContext(Dispatchers.IO) { getMyLocation() }
                openStampDialog(it.stampData)
            }


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
                if(!postStampDialog.isAdded) {
                    postStampDialog.setData(stampData)
                    postStampDialog.show(supportFragmentManager, ".PostStampDialog")
                }
            }
        })
    }

    private fun checkInCircle(myLatLng: LatLng, circle: Circle? = null, circleOptions: CircleOptions? = null): Boolean {

        /// 원의 중심과 반지름
        val circleCenter = circle?.center ?: circleOptions?.center
        val circleRadius = circle?.radius ?: circleOptions?.radius

        val circleLatLng = LatLng(circleCenter!!.latitude , circleCenter.longitude)

        // 결과를 담을 변수
        val distance = FloatArray(1)

        // 거리 측정
        Location.distanceBetween(
            circleLatLng.latitude ,
            circleLatLng.longitude,
            myLatLng.latitude,
            myLatLng.longitude,
            distance
        )

        return distance[0] <= circleRadius!!
    }
}