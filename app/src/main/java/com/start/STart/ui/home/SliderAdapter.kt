package com.start.STart.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.api.banner.BannerModel
import com.start.STart.databinding.ItemSliderBinding

class SliderAdapter : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    var list: List<BannerModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class SliderViewHolder(var binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bannerModel: BannerModel){
            Glide.with(binding.root)
                .load(bannerModel.imageUrl)
                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}