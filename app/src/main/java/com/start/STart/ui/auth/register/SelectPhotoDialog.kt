package com.start.STart.ui.auth.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.skydoves.cloudy.Cloudy
import com.start.STart.R
import com.start.STart.databinding.DialogSelectCollegeBinding
import com.start.STart.databinding.DialogSelectPhotoBinding
import com.start.STart.databinding.ItemDepartmentBinding
import com.start.STart.databinding.LayoutDividerBinding
import com.start.STart.ui.auth.register.SelectCollegeOrDepartmentDialog.Companion.TYPE_COLLEGE
import com.start.STart.ui.auth.register.SelectCollegeOrDepartmentDialog.Companion.TYPE_DEPARTMENT
import com.start.STart.util.contains
import com.start.STart.util.departments
import com.start.STart.util.dp2px
import es.dmoral.toasty.Toasty

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

        }

        binding.btnCamera.setOnClickListener {
            
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