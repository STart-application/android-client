package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonParser
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogPostStampBinding
import com.start.STart.ui.home.festival.FestivalViewModel
import com.start.STart.ui.home.festival.StampData
import com.start.STart.util.contains
import com.start.STart.util.gson
import com.start.STart.util.showErrorToast
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PostStampDialog: DialogFragment() {
    private var _binding: DialogPostStampBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProvider: FusedLocationProviderClient


    var stampData: StampData? = null

    private val viewModel: FestivalViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FestivalViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initBackground()

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())


        binding.btnStamp.setOnClickListener {
            lifecycleScope.launch {
                val myLatLng = withContext(Dispatchers.IO) { getMyLocation() }
                if(checkInCircle(myLatLng, centerLatLng = stampData!!.latLng, radius = 50.0)) {
                    viewModel.postStamp(stampData!!.name)
                } else {
                    Toasty.info(requireContext(), "도장 존으로 이동하여 주세요.").show()
                }
            }

        }
        initLiveDataObservers()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.composeView.setContent { Cloudy(radius = 10, allowAccumulate = { true }){} }
        binding.dim.setOnTouchListener { view, motionEvent ->
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                dismiss()
            }
            true
        }
    }

    private fun initLiveDataObservers() {
        viewModel.postStampResult.observe(this) {
            if(it.isSuccessful) {
                viewModel.loadStamp()
            } else {
                showErrorToast(requireContext(), it.message!!)
            }
        }

        viewModel.loadStampResult.observe(this) {
            if(it.isSuccessful) {
                val jsonData = JsonParser.parseString(gson.toJson((it.data as List<*>).get(0))).asJsonObject
                val isStamped = jsonData.get(stampData!!.name)?.asBoolean?:false

                binding.imageStamp.setImageResource(if(isStamped) stampData!!.drawable_e else stampData!!.drawable)
                binding.btnStamp.apply {
                    text = if(isStamped) "도장찍기 완료!" else "도장찍기!"
                    isEnabled = !isStamped
                }
            } else {
                showErrorToast(requireContext(), "잠시 후 다시 시도해 주세요.")
                dismiss()
            }
        }
    }

    fun setData(stampData: StampData) {
        this.stampData = stampData
    }

    private fun checkInCircle(myLatLng: LatLng, circle: Circle? = null, circleOptions: CircleOptions? = null, centerLatLng: LatLng? = null, radius: Double?): Boolean {

        /// 원의 중심과 반지름
        val circleCenter = circle?.center ?: circleOptions?.center
        val circleRadius = circle?.radius ?: circleOptions?.radius
        val circleLatLng = if(circleRadius != null) {
            LatLng(circleCenter!!.latitude , circleCenter.longitude)
        } else {
            centerLatLng
        }

        // 결과를 담을 변수
        val distance = FloatArray(1)

        // 거리 측정
        Location.distanceBetween(
            circleLatLng!!.latitude ,
            circleLatLng.longitude,
            myLatLng.latitude,
            myLatLng.longitude,
            distance
        )

        return distance[0] <= (circleRadius ?: radius!!)
    }

    @SuppressLint("MissingPermission")
    private suspend fun getMyLocation()  = suspendCoroutine { contination ->
        fusedLocationProvider.lastLocation.addOnSuccessListener {
            contination.resume(LatLng(it.latitude, it.longitude))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadStamp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPostStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}