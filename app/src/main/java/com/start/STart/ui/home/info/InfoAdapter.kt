package com.start.STart.ui.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.databinding.LayoutInfo1Binding
import com.start.STart.databinding.LayoutInfo2Binding
import com.start.STart.databinding.LayoutInfo3Binding

class InfoAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class Info1ViewHolder(val binding: LayoutInfo1Binding): RecyclerView.ViewHolder(binding.root)
    inner class Info2ViewHolder(val binding: LayoutInfo2Binding): RecyclerView.ViewHolder(binding.root)
    inner class Info3ViewHolder(val binding: LayoutInfo3Binding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> {
                Info1ViewHolder(LayoutInfo1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            1 -> {
                Info2ViewHolder(LayoutInfo2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                Info3ViewHolder(LayoutInfo3Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun getItemCount() = 3

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}