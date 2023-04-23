package com.start.STart.ui.home.festival

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.api.ApiClient
import com.start.STart.api.festival.response.PhotoZoneModel
import com.start.STart.databinding.DialogPhotoZoneBinding
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

        binding.photoZone.adapter = photoZoneAdapter
        loadPhotoZone()
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

                        val size = response.body()?.data?.get(0)?.photoList?.size
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