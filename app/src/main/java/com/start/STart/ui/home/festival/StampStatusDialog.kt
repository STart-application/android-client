package com.start.STart.ui.home.festival

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.databinding.DialogStampStatusBinding
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StampStatusDialog : DialogFragment() {

    private var _binding: DialogStampStatusBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setOnClickListener {
            dismiss()
        }

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