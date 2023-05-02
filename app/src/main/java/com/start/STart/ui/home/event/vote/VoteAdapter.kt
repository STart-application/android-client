package com.start.STart.ui.home.event.vote

import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.api.event.vote.Vote
import com.start.STart.databinding.ItemVoteBinding
import com.start.STart.util.showErrorToast
import es.dmoral.toasty.Toasty

class VoteAdapter : RecyclerView.Adapter<VoteAdapter.VoteViewHolder>() {
    var list: List<Vote> = listOf()
        set(value) {
            field = value
        }

    inner class VoteViewHolder(var binding: ItemVoteBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(vote: Vote){
            binding.title.text = vote.title
            binding.start.text = vote.displayStartDate.subSequence(5, 7).toString() + "/" + vote.displayStartDate.subSequence(8, 10) + " " + vote.displayStartDate.subSequence(11, 16).toString()
            binding.end.text = vote.displayEndDate.subSequence(5, 7).toString() + "/" + vote.displayEndDate.subSequence(8, 10) + " " + vote.displayEndDate.subSequence(11, 16).toString()

            val purple_ghost = ContextCompat.getColor(binding.status.context, R.color.dream_purple_ghost)
            val purple = ContextCompat.getColor(binding.status.context, R.color.dream_purple)
            val gray = ContextCompat.getColor(binding.status.context, R.color.dream_gray)
            val green = ContextCompat.getColor(binding.status.context, R.color.dream_green)

            when(vote.status) {
                "START" -> {
                    if(vote.userSelectedOptionIds.isEmpty()) {
                        binding.status.backgroundTintList = ColorStateList.valueOf(green)
                        binding.status.text = "투표 가능"
                    } else {
                        binding.status.backgroundTintList = ColorStateList.valueOf(purple)
                        binding.status.text = "투표 완료"
                    }

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

            binding.root.setOnClickListener {
                val context = binding.root.context
                when(vote.status) {
                    "START" -> {
                        context.startActivity(Intent(context, DetailVoteActivity::class.java).apply {
                            putExtra("vote", vote)


                            if(vote.userSelectedOptionIds.isEmpty())
                                putExtra("vote1", true)
                            else
                                putExtra("vote1", false)

                        })
                    }
                    "END" -> {
                        Toasty.info(context, "투표 가능한 시간이 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                    "BEFORE" -> {
                        Toasty.info(context, "투표 가능한 시간이 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        showErrorToast(binding.root.context)
                    }
                }

            }

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