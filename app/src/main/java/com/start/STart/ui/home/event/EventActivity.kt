package com.start.STart.ui.home.event

import android.graphics.Rect
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.EventModel
import com.start.STart.databinding.ActivityEventBinding
import com.start.STart.util.dp2px
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class EventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEventBinding.inflate(layoutInflater) }
    private val eventAdapter by lazy { EventAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        binding.rvEvent.adapter = eventAdapter

        loadEvent()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.visibility = View.VISIBLE
        binding.toolbar.icSetting.visibility = View.INVISIBLE
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadEvent() {
        ApiClient.eventService.loadEvent()
            .enqueue(object : Callback<EventModel> {
                override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                    if(response.isSuccessful) {
                        var eventSize = response.body()?.data?.size

                        if(eventSize == 0) {
                            binding.noEvent.visibility = View.VISIBLE
                            binding.rvEvent.visibility = View.GONE
                        } else {
                            binding.rvEvent.visibility = View.VISIBLE
                            binding.noEvent.visibility = View.GONE
                            Log.d("tag", eventSize.toString())

                            eventAdapter.list = response.body()!!.data
                            eventAdapter.notifyDataSetChanged()
                            Log.d("tag", eventAdapter.list.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<EventModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                    call.cancel()
                }
            })
    }
}