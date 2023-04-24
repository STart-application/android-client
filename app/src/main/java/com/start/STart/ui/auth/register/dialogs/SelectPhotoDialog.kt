package com.start.STart.ui.auth.register.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogSelectPhotoBinding
import com.start.STart.ui.auth.register.StudentCardUploadActivity
import com.start.STart.ui.auth.register.StudentCardUploadViewModel
import com.start.STart.util.contains

class SelectPhotoDialog: DialogFragment() {
    private var _binding: DialogSelectPhotoBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(StudentCardUploadViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.composeView.setContent {
            Cloudy(radius = 10, allowAccumulate = { true }){

            }
        }

        binding.dim.setOnTouchListener { view, motionEvent ->
            Log.d(null, "onViewCreated: ${motionEvent.rawX} ${motionEvent.rawY}")
            Log.d(null, "onViewCreated: ${binding.cardView.height}")
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                dismiss()
            }
            true
        }

        binding.btnGallery.setOnClickListener {
            (requireActivity() as StudentCardUploadActivity).galleryLauncher.launch("image/*")
            dismiss()
        }

        binding.btnCamera.setOnClickListener {
            (requireActivity() as StudentCardUploadActivity).startCamera()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}