package com.start.STart.ui.home.event

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.Event
import com.start.STart.api.banner.EventModel
import com.start.STart.databinding.ActivityEventBinding
import com.start.STart.util.dp2px
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEventBinding.inflate(layoutInflater) }
    private val eventAdapter by lazy { EventAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()

        loadEvent()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        binding.rvEvent.adapter = eventAdapter
        binding.rvEvent.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val spanCount = 2
                val spacing = dp2px(10f).toInt()

                val position = parent.getChildAdapterPosition(view)
                val column = position % spanCount

                outRect.left = column * spacing / spanCount;

                outRect.right = spacing - (column + 1) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }

            }
        })
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

                            eventAdapter.list = response.body()!!.data as MutableList<Event>
                            eventAdapter.notifyDataSetChanged()

                            eventAdapter.list
                                .sortWith(compareBy<Event> { it ->
                                    when(it.eventStatus) {
                                        "PROCEEDING" -> 0
                                        "BEFORE" -> 1
                                        else -> 2
                                    }
                                }
                                    .thenBy{it.startTime})

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