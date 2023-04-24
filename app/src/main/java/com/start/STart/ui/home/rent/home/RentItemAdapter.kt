package com.start.STart.ui.home.rent.home

import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.databinding.ItemRentBinding
import com.start.STart.ui.home.rent.RentCalendarActivity
import com.start.STart.ui.home.rent.RentItem

class RentItemAdapter : RecyclerView.Adapter<RentItemAdapter.RentItemViewHolder>() {
    var list: List<RentItem> = listOf()
        set(value) {
            field = value
        }

    inner class RentItemViewHolder(val binding: ItemRentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RentItem) {
            binding.textItemName.text = item.category
            binding.imageItem.setImageResource(item.iconDrawable)

            binding.root.setOnClickListener {
                val context = binding.root.context
                context.startActivity(Intent(context, RentCalendarActivity::class.java).apply {
                    putExtra(RentItem.KEY_RENT_ITEM_TYPE, item as Parcelable)
                })
            }
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