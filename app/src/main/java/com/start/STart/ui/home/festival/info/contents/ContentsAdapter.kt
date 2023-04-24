package com.start.STart.ui.home.festival.info.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.databinding.ItemFestivalContentsBinding

class ContentsAdapter: RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>() {
    var list: List<BoothEnum> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ContentsViewHolder(val binding: ItemFestivalContentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(boothEnum: BoothEnum) {
            binding.textTitle.text = boothEnum.boothName
            binding.textPeriodValue.text = boothEnum.locationNPeriod
            binding.textDescriptionValue.text = boothEnum.description
            setCongestion(boothEnum.congestion)

            Glide.with(binding.root)
                .load(boothEnum.drawableRes)
                .centerInside()
                .into(binding.imageTitle)
        }

        private fun setCongestion(congestion: Int) {
            val context = binding.root.context
            val congestionViews = listOf(binding.congestion1, binding.congestion2, binding.congestion3)

            val colors = listOf(
                R.color.dream_gray,
                R.color.dream_green,
                R.color.dream_yellow,
                R.color.dream_red
            )

            for (i in congestionViews.indices) {
                val colorIndex = if (i < congestion) congestion else 0
                val color = ContextCompat.getColor(context, colors[colorIndex])
                congestionViews[i].setBackgroundColor(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsViewHolder {
        val binding = ItemFestivalContentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContentsViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ContentsViewHolder, position: Int) {
        holder.bind(list[position])
    }
}