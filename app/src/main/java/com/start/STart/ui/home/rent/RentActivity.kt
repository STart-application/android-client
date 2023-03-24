package com.start.STart.ui.home.rent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import com.start.STart.R
import com.start.STart.databinding.ActivityRentBinding
import com.start.STart.util.IndentLeadingMarginSpan
import com.start.STart.util.dp2px

class RentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentBinding.inflate(layoutInflater) }

    private val rentItemAdapter by lazy { RentItemAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()

        // TODO: 줄마다 약간의 간격 다름 (보류)
        binding.textDescription.text = SpannableStringBuilder(resources.getString(R.string.rent_description)).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "상시사업 예약"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        binding.rvRentItem.adapter = rentItemAdapter.apply {
            list = listOf(
                RentItem("캐노피", R.drawable.ic_canopy),
                RentItem("듀라테이블", R.drawable.ic_dura_table),
                RentItem("앰프&마이크", R.drawable.ic_amp),
                RentItem("리드선", R.drawable.ic_lead_wire),
                RentItem("L카", R.drawable.ic_cart),
                RentItem("의자", R.drawable.ic_chair),
                RentItem("돗자리", R.drawable.ic_chair),
            )
        }
        binding.rvRentItem.addItemDecoration(RentItemDecoration(spacing = dp2px(10f).toInt()))
    }
}