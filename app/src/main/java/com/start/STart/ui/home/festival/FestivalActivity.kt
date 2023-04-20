package com.start.STart.ui.home.festival

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
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
    private val circleList = mutableListOf<Circle>()

    private val stampDialog by lazy { StampStatusDialog()}


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

        setUpClusterer()

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

        googleMap.setOnCircleClickListener { circle ->
            lifecycleScope.launch {
                val myLatLng = withContext(Dispatchers.IO) { getMyLocation() }

                val circleCenter = circle.center
                val circleRadius = circle.radius
                val distance = FloatArray(1)
                Location.distanceBetween(
                    circleCenter.latitude,
                    circleCenter.longitude,
                    myLatLng.latitude,
                    myLatLng.longitude,
                    distance
                )

                if (distance[0] > circleRadius) {
                    Toasty.info(this@FestivalActivity, "현재 위치가 Circle 내부에 없습니다.").show()
                } else {
                    Toasty.info(this@FestivalActivity, "현재 위치가 Circle 내부에 있습니다.").show()
                }
            }

        }

        googleMap.uiSettings
            .isMyLocationButtonEnabled = false

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))

        binding.locationBtn.setOnClickListener {
            enableLocation()
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

    private fun setUpClusterer() {
        clusterManager = ClusterManager<MarkerModel>(this, googleMap)
        clusterManager.renderer = object: DefaultClusterRenderer<MarkerModel>(this@FestivalActivity, googleMap, clusterManager
        ) {
            override fun onBeforeClusterRendered(
                cluster: Cluster<MarkerModel>,
                markerOptions: MarkerOptions
            ) {
                super.onBeforeClusterRendered(cluster, markerOptions)
                circleList.forEach {
                    it.isVisible = false
                }
                //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this@FestivalActivity, R.drawable.marker_food_truck)))
            }

            override fun getColor(clusterSize: Int): Int {
                return ContextCompat.getColor(this@FestivalActivity, R.color.dream_green)
            }

            override fun onClusterUpdated(cluster: Cluster<MarkerModel>, marker: Marker) {
                super.onClusterUpdated(cluster, marker)
                //marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this@FestivalActivity, R.drawable.marker_food_truck)))
            }

            override fun onBeforeClusterItemRendered(
                item: MarkerModel,
                markerOptions: MarkerOptions
            ) {
                super.onBeforeClusterItemRendered(item, markerOptions)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this@FestivalActivity, R.drawable.marker_stamp_default)))
                circleList.forEach {
                    it.isVisible = true
                }
            }
        }
        googleMap.setOnCameraIdleListener(clusterManager)
        googleMap.setOnMarkerClickListener(clusterManager)
    }

    private fun addMakers(markerModels: List<MarkerModel>) {
        markerModels.forEach {
            clusterManager.addItems(markerModels)
            /*googleMap.addMarker(MarkerOptions()
                .position(it.latLng)
                .title(it.title)
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(this, R.drawable.marker_food_truck)))
            )*/
            val circle: Circle = googleMap.addCircle(
                CircleOptions()
                    .center(it.latLng)
                    .radius(50.0)
                    .clickable(true)
                    .strokeColor(ContextCompat.getColor(this@FestivalActivity, R.color.dream_green))
                    .fillColor(ContextCompat.getColor(this@FestivalActivity, R.color.dream_green_transparent))
            )
            circleList.add(circle)
            circleList.forEach {
                it.isVisible = false
            }
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