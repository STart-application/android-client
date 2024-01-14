package com.start.STart.ui.home.partnership

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.start.STart.api.partner.response.Partner
import com.start.STart.databinding.ItemPartnerBinding
import com.start.STart.ui.util.OnItemClickListener

class PartnerAdapter : RecyclerView.Adapter<PartnerAdapter.ViewHolder>() {

    var originalList: List<Partner> = listOf()
        set(value) {
            field = value
            list = value
        }

    var list: List<Partner> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun filter(category: PartnerCategory, keyword: String? = null) {
        var temp = originalList
            .filter {
                it.partnerTypeId == category.id || category == PartnerCategory.all
            }

        if(!keyword.isNullOrBlank()) {
            temp = temp.filter {
                it.name.contains(keyword) || it.description?.contains(keyword)?:false
            }
        }

        list = temp

    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Partner) {
            Glide.with(binding.imageCover)
                .load(item.imageUrl)
                .centerCrop()
                .into(binding.imageCover)

            binding.textName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(position) }
    }
}