package com.start.STart.ui.home.festival

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import com.start.STart.databinding.DialogStampStatusBinding
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

        val stampImageViewList = listOf(
            binding.stampBungeobang,
            binding.stampPhoto,
            binding.stampGame,
            binding.stampYard,
            binding.stampStage,
        )

        binding.cardView.setOnTouchListener { view, motionEvent ->
            motionEvent.action == MotionEvent.ACTION_DOWN
        }

        binding.root.setOnClickListener {
            if(binding.cardView.contains(it.x.toInt(), it.y.toInt())){
                return@setOnClickListener
            }

            dismiss()
        }

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

    fun load() {
        binding.cardView.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadStamp()
        (requireActivity() as FestivalActivity).showBlur()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (requireActivity() as FestivalActivity).hideCompose()
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