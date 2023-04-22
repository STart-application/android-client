package com.start.STart.ui.home.festival.info.lineup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.festival.response.LineUpData
import com.start.STart.databinding.LayoutLineUpBinding

class LineUpViewPagerAdapter : RecyclerView.Adapter<LineUpViewPagerAdapter.LineUpViewPagerViewHolder>(){

    var list: List<Pair<String, MutableList<LineUpData>>> = listOf()
        set(value) {
            field = value.sortedBy { (k, v) -> k }
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

        fun bind(pair: Pair<String, MutableList<LineUpData>>) {
            calendarAdapter.list = pair.second.sortedBy { it.lineUpTime }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineUpViewPagerViewHolder {
        val binding = LayoutLineUpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LineUpViewPagerViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: LineUpViewPagerViewHolder, position: Int) {
        holder.bind(list[position])
    }
}