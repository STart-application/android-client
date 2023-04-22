package com.start.STart.ui.home.event

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.EventModel
import com.start.STart.databinding.ActivityEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                            binding.noEventText.text = "진행 중인 이벤트가 없습니다."
                            Log.d("tag", "noEvent")
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
                    binding.noEvent.visibility = View.VISIBLE
                    binding.rvEvent.visibility = View.GONE
                    binding.noEventText.text = "이벤트를 불러오지 못했습니다."
                    Log.d("tag", "네트워크통신")
                }
            })
    }



}