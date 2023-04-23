package com.start.STart.ui.home.event

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogEscapeEndBinding
import com.start.STart.databinding.DialogFoodTruckBinding

class EscapeEndDialog: DialogFragment() {
    private var _binding: DialogEscapeEndBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.composeView.setContent {
            Cloudy(radius = 10, allowAccumulate = { true }){

            }
        }

        binding.btn.setOnClickListener {
            startActivity(Intent(context, EventActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            })
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEscapeEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // 뒤로가기 버튼 이벤트를 처리하지 않도록 함
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener true
            }
            false
        }
        Log.d("tag", "onCreateDialog")
        return dialog
    }

}