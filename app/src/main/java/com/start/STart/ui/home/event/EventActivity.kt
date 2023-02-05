package com.start.STart.ui.home.event

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.databinding.ActivityEventBinding


class EventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEventBinding.inflate(layoutInflater) }
    private val eventAdapter by lazy { EventAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvEvent.adapter = eventAdapter.apply {
            list = listOf(
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}