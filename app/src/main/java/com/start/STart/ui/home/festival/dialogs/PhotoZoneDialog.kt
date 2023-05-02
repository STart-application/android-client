package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.cloudy.Cloudy
import com.start.STart.api.ApiClient
import com.start.STart.api.festival.response.PhotoZoneModel
import com.start.STart.databinding.DialogPhotoZoneBinding
import com.start.STart.ui.home.PhotoViewDialog
import com.start.STart.ui.home.festival.info.photozone.PhotoZoneAdapter
import com.start.STart.util.contains
import com.start.STart.util.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoZoneDialog: DialogFragment() {
    private var _binding: DialogPhotoZoneBinding? = null
    private val binding get() = _binding!!

    private val photoZoneAdapter by lazy { PhotoZoneAdapter() }
    private val photoDialog by lazy { PhotoViewDialog() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initBackground()
        initViewPager()
    }

    private fun initViewPager() {
        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = photoZoneAdapter.apply {
            listener = {
                photoDialog.show(requireActivity(), url = it)
            }
        }

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

    private fun loadPhotoZone() {
        ApiClient.festivalService.loadPhotoZones()
            .enqueue(object : Callback<PhotoZoneModel> {
                override fun onResponse(call: Call<PhotoZoneModel>, response: Response<PhotoZoneModel>) {
                    if(response.isSuccessful) {
                        val list = response.body()?.data?.get(0)?.photoList
                        list?.let {
                            photoZoneAdapter.list = list
                            photoZoneAdapter.notifyDataSetChanged()
                        }
                    } else {
                        showErrorToast(requireContext())
                    }
                }

                override fun onFailure(call: Call<PhotoZoneModel>, t: Throwable) {
                    showErrorToast(requireContext())
                }
            })
    }

}