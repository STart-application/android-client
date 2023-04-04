package com.start.STart.ui.home.event

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.databinding.ActivityEventBinding
import com.start.STart.util.dp2px


class EventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEventBinding.inflate(layoutInflater) }
    private val eventAdapter by lazy { EventAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initToolbar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
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

        binding.rvEvent.addItemDecoration(object : RecyclerView.ItemDecoration() {
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

                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        })
    }

    private fun initToolbar(){
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "이벤트 참여"
    }
}