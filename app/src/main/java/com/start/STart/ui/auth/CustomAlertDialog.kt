package com.start.STart.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogAlertBinding

class CustomAlertDialog : DialogFragment() {

    private var _binding: DialogAlertBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            BlurScreen()
        }

        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }

    fun setTitle(title: String?): CustomAlertDialog {
        binding.textTitle.text = title
        return this
    }

    fun setCaption(caption: String?): CustomAlertDialog {
        binding.textCaption.text = caption
        return this
    }

    @Composable
    fun BlurScreen() {
        Cloudy(radius = 20) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}