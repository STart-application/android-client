package com.start.STart.ui.home.rent

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.start.STart.api.rent.request.PostRentBody
import com.start.STart.databinding.ActivityRentBinding
import com.start.STart.util.getParcelableExtra
import com.start.STart.util.showErrorToast
import com.start.STart.util.showSuccessToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RentActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRentBinding.inflate(layoutInflater) }
    private val viewModel: RentViewModel by viewModels()

    private val datePicker by lazy {
        MaterialDatePicker.Builder.dateRangePicker()
            .build()
    }

    val requestFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())



    private lateinit var rentItem: RentItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        rentItem = intent.getParcelableExtra(key = RentItem.KEY_RENT_ITEM_TYPE)!!

        binding.textItemName.text = rentItem.category
        binding.textCaution.text = rentItem.caution

        initRentListeners()
        initPostRentResultLiveData()
    }

    private fun initRentListeners() {
        binding.btnSetPeriod.setOnClickListener {
            datePicker.show(supportFragmentManager, null)
        }

        datePicker.addOnPositiveButtonClickListener { pair ->
            val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
            val dateFormatter2 = SimpleDateFormat("MM.dd", Locale.getDefault())
            val formattedStartDate = dateFormatter.format(Date(pair.first))
            val formattedEndDate = dateFormatter2.format(Date(pair.second))
            val formattedDateRange = "$formattedStartDate - $formattedEndDate"

            binding.inputPeriod.text = formattedDateRange
            updateBtn()
        }

        binding.inputPurpose.addTextChangedListener {
            updateBtn()
        }

        binding.btnAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            updateBtn()
        }

        binding.btnMinusCount.setOnClickListener {
            val current = binding.inputCount.text.toString().toInt()
            if(current > 0) {
                binding.inputCount.setText((current - 1).toString())
            }
            updateBtn()
        }

        binding.btnPlusCount.setOnClickListener {
            val current = binding.inputCount.text.toString().toInt()
            binding.inputCount.setText((current + 1).toString())
            updateBtn()
        }

        binding.btnRent.setOnClickListener {
            viewModel.postRent(PostRentBody(
                binding.inputPurpose.text.toString(),
                binding.inputCount.text.toString().toInt(),
                rentItem.name,
                requestFormatter.format(datePicker.selection?.first),
                requestFormatter.format(datePicker.selection?.second),
            ))
        }
    }

    private fun updateBtn() {
        binding.btnRent.isEnabled =
            binding.inputPeriod.text.isNotBlank() &&
            binding.inputPurpose.text.isNotBlank() &&
            binding.inputCount.text.toString() != "0" &&
            binding.btnAgree.isChecked
    }

    private fun initPostRentResultLiveData() {
        viewModel.postRentResult.observe(this) { result ->
            if(result.isSuccessful) {
                showSuccessToast(this, result.message)
                finish()
            } else {
                showErrorToast(this, result.message)
            }
        }
    }
}