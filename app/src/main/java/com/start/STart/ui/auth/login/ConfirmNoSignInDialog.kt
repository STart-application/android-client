package com.start.STart.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmNoSignInDialog : DialogFragment() {

    private var _binding: DialogNoSignInBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            BlurScreen()
        }

        binding.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    PreferenceManager.putBoolean(Constants.KEY_AGREE_WITHOUT_LOGIN, true)
                }
                startActivity(Intent(context, HomeActivity::class.java))
                activity?.finish()
            }
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