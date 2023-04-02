package com.start.STart.ui.home.rent.myrent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.api.rent.response.MyRentData
import com.start.STart.databinding.ItemMyRentBinding
import com.start.STart.ui.home.rent.RentItem
import com.start.STart.ui.home.rent.RentStatus
import org.threeten.bp.format.DateTimeFormatter

class MyRentAdapter : RecyclerView.Adapter<MyRentAdapter.MyRentViewHolder>(){
    var list = listOf<MyRentData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    inner class MyRentViewHolder(val binding: ItemMyRentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(myRentData: MyRentData) {

            val rentItem = RentItem.values().find { it.name == myRentData.itemCategory }
            binding.textItemName.text = rentItem?.category
            binding.textCountValue.text = myRentData.account.toString()
            DateTimeFormatter.ofPattern("yyyy. MM. dd")
            binding.textDateValue.text = formatDate(myRentData.startTime, myRentData.endTime)

            RentStatus.values().find { it.name == myRentData.rentStatus }?.let {
                binding.textStatus.text = it.description
                binding.textStatus.backgroundTintList = ContextCompat.getColorStateList(binding.root.context, it.color)
            }

        }

        private fun formatDate(startTime: String, endTime: String): String {
            val startDateStr = "${startTime.slice(0..3)}. ${startTime.slice(5..6)}. ${startTime.slice(8..9)}"
            val endDateStr = "${endTime.slice(5..6)}. ${endTime.slice(8..9)}"
            return "$startDateStr - $endDateStr"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRentViewHolder {
        val binding = ItemMyRentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRentViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MyRentViewHolder, position: Int) {
        holder.bind(list[position])
    }


}