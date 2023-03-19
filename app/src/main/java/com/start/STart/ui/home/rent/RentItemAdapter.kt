package com.start.STart.ui.home.rent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.databinding.ItemRentBinding

class RentItemAdapter : RecyclerView.Adapter<RentItemAdapter.RentItemViewHolder>() {
    var list: List<RentItem> = listOf()
        set(value) {
            field = value
        }

    inner class RentItemViewHolder(val binding: ItemRentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RentItem) {
            binding.textItemName.text = item.name
            binding.imageItem.setImageResource(item.drawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentItemViewHolder {
        val binding = ItemRentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RentItemViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RentItemViewHolder, position: Int) {
        holder.bind(list[position])
    }
}