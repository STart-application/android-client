package com.start.STart.ui.home.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.start.STart.R
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityPaymentBinding
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import com.start.STart.util.getCollegeByDepartment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPaymentBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()
        loadMember()
    }

    private fun initToolbar() {
        binding.toolbar.btnBack.setOnClickListener { finish() }
        binding.toolbar.textTitle.text = "자치회비 납부 확인"
    }

    private fun loadMember() = lifecycleScope.launch(Dispatchers.IO) {
        val memberData = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
        withContext(Dispatchers.Main) {
            if(memberData != null) {
                setMemberData(memberData)
            } else {
                finish()
            }
        }
    }

    private fun setMemberData(memberData: MemberData) {
        binding.name.text = memberData.name
        binding.studentId.text = memberData.studentNo
        binding.department.text = memberData.department
        binding.college.text = getCollegeByDepartment(memberData.department)

        binding.payment.text = if(memberData.memberShip) "자치회비 납부자" else "자취회비 미납부자"
        binding.payment.setTextColor(ContextCompat.getColor(this@PaymentActivity,
            if(memberData.memberShip) R.color.dream_purple else R.color.dream_yellow)
        )

        binding.image.setImageResource(if(memberData.memberShip) R.drawable.membership else R.drawable.membershipnono)
    }
}