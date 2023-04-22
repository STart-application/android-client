package com.start.STart.ui.home.festival.info.lineup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.festival.response.LineUpData
import com.start.STart.databinding.LayoutLineUpBinding

class LineUpViewPagerAdapter : RecyclerView.Adapter<LineUpViewPagerAdapter.LineUpViewPagerViewHolder>(){

    var list: Map<String, MutableList<LineUpData>> = mapOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class LineUpViewPagerViewHolder(val binding: LayoutLineUpBinding): RecyclerView.ViewHolder(binding.root) {
        val calendarAdapter = TimeLineAdapter()

        init {
            initRecyclerView()
        }

        private fun initRecyclerView() {
            binding.root.adapter = calendarAdapter
        }

        fun bind(key: String) {
            calendarAdapter.list = list[key]!!.toList()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineUpViewPagerViewHolder {
        val binding = LayoutLineUpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LineUpViewPagerViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: LineUpViewPagerViewHolder, position: Int) {
        val iterator = list.entries.iterator()
        for((idx, entry) in iterator.withIndex()) {
            if(idx == position) {
                holder.bind(entry.key)
            }
        }
    }
}