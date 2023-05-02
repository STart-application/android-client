package com.start.STart.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogPhotoViewBinding
import com.start.STart.util.contains

class PhotoViewDialog : DialogFragment() {

    private var _binding: DialogPhotoViewBinding? = null
    private val binding get() = _binding!!

    private var imageUrl: String? = null
    private var resourceId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackground()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.composeView.setContent { Cloudy(radius = 10, allowAccumulate = { true }){} }
        binding.dim.setOnTouchListener { view, motionEvent ->
            if(!binding.scaleImageView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                dismiss()
            }
            true
        }
    }

    fun setData(resourceId: Int? = null) {
        this.resourceId = resourceId
    }

    fun setData(url: String? = null) {
        this.imageUrl = url
    }

    override fun onStart() {
        super.onStart()

        val loadData = this.imageUrl?:this.resourceId

        Glide.with(binding.root)
            .asBitmap()
            .load(loadData)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.scaleImageView.setImage(ImageSource.bitmap(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) { }
            })

    }

    fun show(activity: FragmentActivity, tag: String? = null, resourceId: Int? = null, url: String? = null) {
        if(!isAdded) {
            this.resourceId = resourceId
            this.imageUrl = url
            super.show(activity.supportFragmentManager, tag)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPhotoViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}