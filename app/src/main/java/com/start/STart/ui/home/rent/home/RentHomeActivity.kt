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
import com.start.STart.ui.auth.login.LoginOrSkipActivity
import com.start.STart.ui.auth.util.AuthenticationUtil
import com.start.STart.ui.home.rent.RentItem
import com.start.STart.ui.home.rent.myrent.MyRentActivity
import com.start.STart.ui.home.setting.ConfirmDialog
import com.start.STart.util.PreferenceManager
import com.start.STart.util.dp2px
import com.start.STart.util.getCollegeByDepartment

class RentHomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentHomeBinding.inflate(layoutInflater) }
    private val viewModel: RentHomeViewModel by viewModels()

    private val rentItemAdapter by lazy { RentItemAdapter() }

    private val loginConfirmDialog by lazy { ConfirmDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        initToolbar()
        initButton()

        initRecyclerView()
    }

    private fun initData() {
        AuthenticationUtil.performActionOnLogin({
            bindMemberData(it.loggedInMemberData)
        }, failListener = {
            enableNotLogin()
        })
    }

    private fun initButton() {
        binding.btnMyRent.setOnClickListener {
            AuthenticationUtil.performActionOnLogin({
                startActivity(Intent(this, MyRentActivity::class.java))
            })
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
            list = RentItem.values().toList()
        }
        binding.rvRentItem.addItemDecoration(RentItemDecoration(spacing = dp2px(6f).toInt()))
    }

    private fun enableNotLogin() {
        binding.layoutProfile.visibility = View.GONE
        binding.layoutLogin.visibility = View.VISIBLE

        binding.imageProfile.setColorFilter(ContextCompat.getColor(this, R.color.dream_gray))
        binding.btnMyRent.backgroundTintList = ContextCompat.getColorStateList(this, R.color.dream_gray_d9)
        binding.btnMyRent.setTextColor(ContextCompat.getColor(this, R.color.text_caption))
        binding.btnMyRent.isEnabled = false

        binding.btnLogin.setOnClickListener {

            if(!loginConfirmDialog.isAdded) {
                loginConfirmDialog.setData("로그인 화면으로 이동합니다.", onConfirm = {
                    PreferenceManager.clear()
                    startActivity(Intent(this, LoginOrSkipActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }).show(supportFragmentManager, null)
            }
        }
    }

    private fun bindMemberData(memberData: MemberData?) {
        binding.layoutProfile.visibility = View.VISIBLE
        binding.layoutLogin.visibility = View.GONE

        memberData?.let {
            binding.textName.text= it.name
            binding.textStudentId.text = it.studentNo
            binding.textDepartment.text = it.department
            binding.textCollege.text = getCollegeByDepartment(it.department)
        }
    }
}