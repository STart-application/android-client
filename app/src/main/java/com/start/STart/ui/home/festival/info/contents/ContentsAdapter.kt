package com.start.STart.ui.home.festival.info.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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