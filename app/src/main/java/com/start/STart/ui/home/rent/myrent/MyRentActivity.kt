package com.start.STart.ui.home.rent.myrent

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.rent.response.MyRentData
import com.start.STart.databinding.ActivityMyRentBinding
import com.start.STart.util.dp2px
import com.start.STart.util.showErrorToast

class MyRentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyRentBinding.inflate(layoutInflater) }
    private val viewModel: MyRentViewModel by viewModels()
    private val myRentAdapter by lazy { MyRentAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        initRecyclerView()
        initLoadMyRentDataLiveData()

        viewModel.loadMyRent()
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "내 예약"
    }

    private fun initRecyclerView() {
        binding.myRentRecyclerView.adapter = myRentAdapter
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

    private fun initLoadMyRentDataLiveData() {
        viewModel.loadMyRentResult.observe(this) { result ->
            if(result.isSuccessful) {
                val list = (result.data as List<MyRentData>).sortedByDescending { it.rentId }
                if(list.isNotEmpty()) {
                    binding.myRentRecyclerView.visibility = View.VISIBLE
                    binding.layoutEmpty.visibility = View.GONE
                    myRentAdapter.list = list
                } else {
                    binding.myRentRecyclerView.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                }
            } else {
                binding.textInfo.text = "잠시 후 다시 시도해주세요."
                showErrorToast(this, result.message)
            }
        }
    }
}