package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.cloudy.Cloudy
import com.start.STart.R
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.BannerModel
import com.start.STart.api.festival.response.PhotoZoneModel
import com.start.STart.databinding.DialogPhotoZoneBinding
import com.start.STart.ui.home.SliderAdapter
import com.start.STart.ui.home.festival.info.photozone.PhotoZoneAdapter
import com.start.STart.util.contains
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoZoneDialog: DialogFragment() {
    private var _binding: DialogPhotoZoneBinding? = null
    private val binding get() = _binding!!
    private val photoZoneAdapter by lazy { PhotoZoneAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initBackground()

        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = photoZoneAdapter

        TabLayoutMediator(binding.indicator, binding.slider) { _, _ ->
        }.attach()

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

    override fun onStart() {
        super.onStart()
        loadPhotoZone()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPhotoZoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadPhotoZone() {
        ApiClient.festivalService.loadPhotoZones()
            .enqueue(object : Callback<PhotoZoneModel> {
                override fun onResponse(call: Call<PhotoZoneModel>, response: Response<PhotoZoneModel>) {
                    if(response.isSuccessful) {

                        photoZoneAdapter.list = response.body()!!.data[0].photoList
                        photoZoneAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<PhotoZoneModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }

}