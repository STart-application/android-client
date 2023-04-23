package com.start.STart.ui.home.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogConfirmBinding
import com.start.STart.util.contains

class ConfirmDialog: DialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private lateinit var title: String
    private var onCancel: OnClickListener? = null
    private lateinit var onConfirm: OnClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackground()
    }

    override fun onStart() {
        super.onStart()

        binding.textTitle.text = title
        binding.btnCancel.setOnClickListener(onCancel?: OnClickListener {
            dismiss()
        })
        binding.btnConfirm.setOnClickListener(onConfirm)
    }

    fun setData(title: String, onCancel: OnClickListener? = null, onConfirm: OnClickListener) {
        this.title = title
        this.onCancel = onCancel
        this.onConfirm = onConfirm
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBackground() {
        binding.composeView.setContent { Cloudy(radius = 10, allowAccumulate = { true }){} }
        binding.dim.setOnTouchListener { view, motionEvent ->
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                (onCancel?: OnClickListener {
                    dismiss()
                }).onClick(binding.btnCancel)
            }
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}