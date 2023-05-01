package com.start.STart.ui.home.event.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.event.vote.Vote
import com.start.STart.databinding.ItemVoteListBinding
import com.start.STart.ui.home.event.vote.VoteListAdapter.VoteListViewHolder

class VoteListAdapter : RecyclerView.Adapter<VoteListViewHolder>() {
    var list: List<Vote> = mutableListOf()

        set(value) {
            field = value
        }

    inner class VoteListViewHolder(var binding: ItemVoteListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(vote: Vote){


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteListViewHolder {
        val binding = ItemVoteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}



