package com.start.STart.ui.auth.login

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.start.STart.databinding.DialogNoSignInBinding
import com.start.STart.ui.home.HomeActivity
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import com.start.STart.util.contains
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmNoSignInDialog : DialogFragment() {

    private var _binding: DialogNoSignInBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    PreferenceManager.putBoolean(Constants.KEY_AGREE_WITHOUT_LOGIN, true)
                }
                startActivity(Intent(context, HomeActivity::class.java))
                activity?.finish()
            }
        }

        binding.cardView.setOnTouchListener { view, motionEvent ->
            motionEvent.action == MotionEvent.ACTION_DOWN
        }

        binding.root.setOnClickListener {
            if(binding.cardView.contains(it.x.toInt(), it.y.toInt())){
                return@setOnClickListener
            }

            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (requireActivity() as LoginOrSkipActivity).hideCompose()
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