package com.start.STart.ui.home.event.vote

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.api.banner.Vote
import com.start.STart.databinding.ItemVoteBinding

class VoteAdapter : RecyclerView.Adapter<VoteAdapter.VoteViewHolder>() {

    var list: List<Vote> = mutableListOf()

        set(value) {
            field = value
        }

    inner class VoteViewHolder(var binding: ItemVoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(vote: Vote){
            binding.title.text = vote.title

            binding.root.setOnClickListener {
                val context = binding.root.context
                context.startActivity(Intent(context, DetailVoteActivity::class.java))
            }

            /*
            val purple_ghost = ContextCompat.getColor(binding.status.context, R.color.dream_purple_ghost)
            val purple = ContextCompat.getColor(binding.status.context, R.color.dream_purple)
            val gray = ContextCompat.getColor(binding.status.context, R.color.dream_gray)
            val green = ContextCompat.getColor(binding.status.context, R.color.dream_green)

            when(vote.status) {
                "PROCEEDING" -> {
                    binding.status.backgroundTintList = ColorStateList.valueOf(green)
                    binding.status.text = "투표 가능"
                }
                "END" -> {
                    binding.status.backgroundTintList = ColorStateList.valueOf(gray)
                    binding.status.text = "투표 마감"
                }
                "BEFORE" -> {
                    binding.status.backgroundTintList = ColorStateList.valueOf(purple_ghost)
                    binding.status.text = "투표 예정"
                }
            }

             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}