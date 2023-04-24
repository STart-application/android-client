package com.start.STart.ui.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.start.STart.api.banner.BannerModel
import com.start.STart.databinding.DialogPhotoViewBinding
import com.start.STart.ui.home.info.InfoActivity
import com.start.STart.util.contains

class PhotoViewDialog : DialogFragment() {

    private var _binding: DialogPhotoViewBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            if(binding.photoView2.contains(it.x.toInt(), it.y.toInt())){
                return@setOnClickListener
            }

            dismiss()
        }
    }

    fun setImage(bannerModel: BannerModel) {
        Glide.with(binding.root)
            .asBitmap()
            .load(bannerModel.imageUrl?:bannerModel.imageDrawable)
            .into(object: CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.photoView2.setImage(ImageSource.bitmap(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    fun setImage(resourceId: Int) {
        binding.photoView2.setImage(ImageSource.resource(resourceId))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onStart() {
        super.onStart()
        if(activity is HomeActivity) {
            (activity as HomeActivity).setImage()
        } else if(activity is InfoActivity) {
            (activity as InfoActivity).setImage()
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