package com.start.STart.ui.home.setting.updatehistory

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.databinding.ItemUpdateHistoryBinding

class UpdateHistoryAdapter: RecyclerView.Adapter<UpdateHistoryAdapter.UpdateHistoryViewHolder>() {

    var list: List<UpdateHistory> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class UpdateHistoryViewHolder(var binding: ItemUpdateHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: UpdateHistory) {
            history.let {
                binding.textVersion.text = it.version
                binding.textDate.text = it.dateString

                it.historyList.forEach { history ->
                    binding.layoutHistory.addView(TextView(binding.root.context).apply {
                        text = history
                    }.apply {
                        layoutParams = LinearLayoutCompat.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT,
                        ).apply {
                            setMargins(0, 10.toDp(binding.root.context), 0, 0)
                        }
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateHistoryViewHolder {
        val binding = ItemUpdateHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdateHistoryViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: UpdateHistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun Int.toDp(context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
    }


}