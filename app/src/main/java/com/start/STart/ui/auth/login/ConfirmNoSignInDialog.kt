package com.start.STart.ui.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.util.contains

class ConfirmNoSignInDialog : DialogFragment() {

    private var _binding: DialogNoSignInBinding? = null
    private val binding get() = _binding!!

    private var onCancel: View.OnClickListener? = null
    private lateinit var onConfirm: View.OnClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackground()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.composeView.setContent { Cloudy(radius = 10, allowAccumulate = { true }){} }
        binding.dim.setOnTouchListener { view, motionEvent ->
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                binding.btnCancel.callOnClick()
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()

        binding.btnCancel.setOnClickListener(onCancel?: View.OnClickListener {
            dismiss()
        })
        binding.btnConfirm.setOnClickListener(onConfirm)
    }

    fun setData(onCancel: View.OnClickListener? = null, onConfirm: View.OnClickListener) {
        this.onCancel = onCancel
        this.onConfirm = onConfirm
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNoSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}