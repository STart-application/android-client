package com.start.STart.ui.home.pay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityPaymentBinding
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
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
        binding.toolbar.textTitle.text = "자치회비 납부 확인"
        binding.toolbar.btnBack.visibility = View.VISIBLE
        binding.toolbar.icSetting.visibility = View.INVISIBLE
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadMember() = lifecycleScope.launch(Dispatchers.IO) {
        val memberData = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
        withContext(Dispatchers.Main) {
            binding.name.text = memberData?.name
            binding.studentId.text = memberData?.studentNo
            binding.department.text = memberData?.department

            if(!memberData!!.memberShip) {
                binding.payment.text = "자치회비 미납부자"
                binding.payment.setTextColor(ContextCompat.getColor(this@PaymentActivity, R.color.dream_yellow))

                binding.image.setImageResource(R.drawable.membershipnono)
            } else {
                binding.payment.text = "자치회비 납부자"
                binding.payment.setTextColor(ContextCompat.getColor(this@PaymentActivity, R.color.dream_purple))

                binding.image.setImageResource(R.drawable.membership)
            }

        }
    }
}