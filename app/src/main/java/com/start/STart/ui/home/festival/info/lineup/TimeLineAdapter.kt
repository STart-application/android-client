package com.start.STart.ui.home.festival.info.lineup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import com.start.STart.R
import com.start.STart.api.festival.response.LineUpData
import com.start.STart.databinding.ItemLineUpBinding

class TimeLineAdapter: RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {
    var list: List<LineUpData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class TimeLineViewHolder(val binding: ItemLineUpBinding, viewType: Int): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timeline.initLine(viewType)
        }

        fun bind(lineUpData: LineUpData) {
            binding.timeline.marker = ContextCompat.getDrawable(binding.root.context, R.drawable.marker_line_up)
            binding.textTime.text = lineUpData.lineUpTime.substring(11, 16)
            binding.textDescription.text = lineUpData.lineUpTitle
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        val binding = ItemLineUpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeLineViewHolder(binding, viewType)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(list[position])
    }
}