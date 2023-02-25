package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.ui.home.HomeActivity

class ConfirmNoSignInDialog : DialogFragment() {

    private var _binding: DialogNoSignInBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            BlurScreen()
        }

        binding.btnConfirm.setOnClickListener {
            startActivity(Intent(context, HomeActivity::class.java))
            activity?.finish()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

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
        _binding = DialogNoSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}