package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogPostStampBinding
import com.start.STart.ui.home.festival.FestivalViewModel
import com.start.STart.ui.home.festival.StampData
import com.start.STart.util.contains
import com.start.STart.util.gson
import com.start.STart.util.showErrorToast

class PostStampDialog: DialogFragment() {
    private var _binding: DialogPostStampBinding? = null
    private val binding get() = _binding!!

    var stampData: StampData? = null
    var isInCircle: Boolean = false

    private val viewModel: FestivalViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FestivalViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initBackground()

        binding.btnStamp.setOnClickListener {
            viewModel.postStamp(stampData!!.name)
        }
        initLiveDataObservers()
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

    private fun initLiveDataObservers() {
        viewModel.postStampResult.observe(this) {
            if(it.isSuccessful) {
                viewModel.loadStamp()
            } else {
                showErrorToast(requireContext(), it.message!!)
            }
        }

        viewModel.loadStampResult.observe(this) {
            if(it.isSuccessful) {
                val jsonData = JsonParser.parseString(gson.toJson((it.data as List<*>).get(0))).asJsonObject
                val isStamped = jsonData.get(stampData!!.name).asBoolean

                binding.imageStamp.setImageResource(if(isStamped) stampData!!.drawable_e else stampData!!.drawable)
                binding.btnStamp.apply {
                    text = if(isStamped) "도장찍기 완료!" else "도장찍기!"
                    isEnabled = !isStamped
                }
            } else {
                viewModel.loadStamp()
                showErrorToast(requireContext(), "잠시 후 다시 시도해 주세요.")
                dismiss()
            }
        }
    }

    fun setData(stampData: StampData) {
        this.stampData = stampData
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadStamp()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPostStampBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}