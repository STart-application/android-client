package com.start.STart.ui.home.rent.myrent

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.api.rent.response.MyRentData
import com.start.STart.databinding.ActivityMyRentBinding
import com.start.STart.util.dp2px

class MyRentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyRentBinding.inflate(layoutInflater) }
    private val myRentAdapter by lazy { MyRentAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

            initToolbar()
        initRecyclerView()

    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "내 예약"
    }

    private fun initRecyclerView() {
        binding.myRentRecyclerView.adapter = myRentAdapter.apply {
            list = listOf(
                MyRentData(0, 1, "취미", "CONFIRM", "CHAIR", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 1, "취미", "DONE", "CART", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 3, "취미", "DENY", "CANOPY", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 1, "취미", "WAIT", "MAT", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 5, "취미", "RENT", "TABLE", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 1, "취미", "CONFIRM", "SIMPLE_TABLE", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 7, "취미", "DENY", "WIRE", "2023-05-22", "2024-07-23", "", ""),
                MyRentData(0, 1, "취미", "WAIT", "AMP", "2023-05-22", "2024-07-23", "", ""),
            )
        }
        binding.myRentRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val spacing = dp2px(15f).toInt()

                val position = parent.getChildAdapterPosition(view)

                if (position >= 1) {
                    outRect.top = spacing
                }
            }
        })
    }
}