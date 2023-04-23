package com.start.STart.ui.home.rent.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.start.STart.R
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityRentHomeBinding
import com.start.STart.ui.home.rent.RentItem
import com.start.STart.ui.home.rent.myrent.MyRentActivity
import com.start.STart.util.dp2px
import com.start.STart.util.getCollegeByDepartment

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

        binding.btnMyRent.setOnClickListener {
            startActivity(Intent(this, MyRentActivity::class.java))
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
            list = RentItem.values().toList()
        }
        binding.rvRentItem.addItemDecoration(RentItemDecoration(spacing = dp2px(6f).toInt()))
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

        binding.imageProfile.setColorFilter(ContextCompat.getColor(this, R.color.dream_gray))
        binding.btnMyRent.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dream_gray_d9)
        binding.btnMyRent.setTextColor(ContextCompat.getColor(this, R.color.text_caption))
        binding.btnMyRent.isEnabled = false
    }

    private fun bindMemberData(memberData: MemberData) {
        binding.layoutProfile.visibility = View.VISIBLE
        binding.layoutLogin.visibility = View.GONE

        binding.textName.text= memberData.name
        binding.textStudentId.text = memberData.studentNo
        binding.textDepartment.text = memberData.department
        binding.textCollege.text = getCollegeByDepartment(memberData.department)
    }
}