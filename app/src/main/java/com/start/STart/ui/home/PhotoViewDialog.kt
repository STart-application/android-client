package com.start.STart.ui.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.bumptech.glide.Glide
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.databinding.DialogPhotoViewBinding
import com.start.STart.databinding.DialogStampStatusBinding
import com.start.STart.ui.home.HomeActivity
import com.start.STart.ui.home.festival.FestivalActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import com.start.STart.util.contains
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoViewDialog : DialogFragment() {

    private var _binding: DialogPhotoViewBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            if(binding.photoView.contains(it.x.toInt(), it.y.toInt())){
                return@setOnClickListener
            }

            dismiss()
        }
    }

    fun setImage(url: String) {
        Glide.with(binding.root)
            .load(url)
            .into(binding.photoView)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }

    override fun onStart() {
        super.onStart()
        (activity as HomeActivity).setImage()
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