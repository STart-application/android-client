package com.start.STart.ui.home.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.start.STart.R
import com.start.STart.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailEventBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.visibility = View.VISIBLE
        binding.toolbar.icSetting.visibility = View.INVISIBLE
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }
}