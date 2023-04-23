package com.start.STart.ui.home.festival.info.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.festival.response.BoothData
import com.start.STart.databinding.ItemFestivalContentsBinding

class ContentsAdapter: RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>() {
    var list: List<BoothData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ContentsViewHolder(val binding: ItemFestivalContentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(boothData: BoothData) {
            // TODO: 부스 정해진 기획 내용 받아야 함
            binding.textTitle.text = boothData.name
            binding.textPeriodValue.text = "11:00 - 18:00"
            binding.textDescriptionValue.text = "자치회비 납부자 - 무료\n자치회비 미납부자 - 500원\n외부 참가자 - 2,000원"
            setCongestion(boothData.congestion)

            val context = binding.root.context
            Glide.with(binding.root)
                .load(context.getString(R.string.url_polar_bear))
                .centerCrop()
                .into(binding.imageTitle)
        }

        fun setCongestion(congestion: Int) {
            val context = binding.root.context
            val congestionViews = listOf(binding.congestion1, binding.congestion2, binding.congestion3)

            for (i in congestionViews.indices) {
                congestionViews[i].setBackgroundColor(
                    if (i < congestion) ContextCompat.getColor(context, R.color.dream_green)
                    else ContextCompat.getColor(context, R.color.dream_gray)
                )
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