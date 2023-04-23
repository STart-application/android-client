package com.start.STart.ui.home.festival.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogStampStatusBinding
import com.start.STart.ui.home.festival.FestivalViewModel
import com.start.STart.ui.home.festival.StampData
import com.start.STart.util.*

class StampStatusDialog : DialogFragment() {

    private var _binding: DialogStampStatusBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FestivalViewModel by lazy {
        ViewModelProvider(requireActivity()).get(FestivalViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackground()

        val stampImageViewList = listOf(
            binding.stampBungeobang,
            binding.stampPhoto,
            binding.stampGame,
            binding.stampYard,
            binding.stampStage,
        )

        viewModel.loadStampResult.observe(this) {
            if(it.isSuccessful) {
                val jsonData = JsonParser.parseString(gson.toJson((it.data as List<*>).get(0))).asJsonObject
                Log.d(null, "onViewCreated: $jsonData")

                for((idx, type) in StampData.values().withIndex()) {
                    if(jsonData.get(type.name).asBoolean) {
                        stampImageViewList[idx].setImageResource(type.drawable_e)
                    } else {
                        stampImageViewList[idx].setImageResource(type.drawable)
                    }

                }

            } else {
                viewModel.loadStamp()
                showErrorToast(requireContext(), "잠시 후 다시 시도해 주세요.")
                dismiss()
            }
        }
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
        viewModel.loadStamp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogStampStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}