package com.start.STart.ui.home.rent

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.start.STart.R
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityRentHomeBinding
import com.start.STart.util.IndentLeadingMarginSpan
import com.start.STart.util.dp2px

class RentHomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentHomeBinding.inflate(layoutInflater) }
    private val viewModel: RentHomeViewModel by viewModels()

    private val rentItemAdapter by lazy { RentItemAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        viewModel.loadMemberData()
    }

    private fun initView() {
        initToolbar()
        initRecyclerView()

        // TODO: 줄마다 약간의 간격 다름 (보류)
        binding.textDescription.text = SpannableStringBuilder(resources.getString(R.string.rent_description)).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }

        initViewModelListeners()
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
                RentItem("CANOPY", "캐노피", R.drawable.ic_canopy),
                RentItem("TABLE", "듀라테이블", R.drawable.ic_dura_table),
                RentItem("AMP", "앰프&마이크", R.drawable.ic_amp),
                RentItem("WIRE", "리드선", R.drawable.ic_lead_wire),
                RentItem("CART","L카", R.drawable.ic_cart),
                RentItem("CHAIR", "의자", R.drawable.ic_chair),
                RentItem("", "돗자리", R.drawable.ic_chair),// TODO: 대여 아이템 추가
            )
        }
        binding.rvRentItem.addItemDecoration(RentItemDecoration(spacing = dp2px(10f).toInt()))
    }

    private fun initViewModelListeners() {
        viewModel.memberData.observe(this) {
            if(it.isSuccessful) {
                bindMemberData(it.data as MemberData)
            } else {
                enableNotLogin()
            }
        }
    }

    private fun enableNotLogin() {
        binding.layoutProfile.visibility = View.GONE
        binding.layoutLogin.visibility = View.VISIBLE

        binding.imageProfile.setColorFilter(ContextCompat.getColor(this, R.color.dream_gray_light))
        binding.btnMyRent.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dream_gray_light_d)
        binding.btnMyRent.setTextColor(ContextCompat.getColor(this, R.color.dream_gray))
        binding.btnMyRent.isEnabled = false
    }

    private fun bindMemberData(memberData: MemberData) {
        binding.layoutProfile.visibility = View.VISIBLE
        binding.layoutLogin.visibility = View.GONE

        binding.textName.text= memberData.name
        binding.textStudentId.text = memberData.studentNo
        binding.textDepartment.text = memberData.department
        binding.textCollege.text = "찾아"
    }
}