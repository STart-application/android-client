package com.start.STart.ui.home.event.vote

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.VoteModel
import com.start.STart.databinding.ActivityDetailVoteBinding
import com.start.STart.databinding.ItemVoteListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVoteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailVoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        val votingId = intent.getIntExtra("vote", 0)
        Log.d("tag", votingId.toString())
        loadDetailVote(votingId)
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun loadDetailVote(votingId: Int) {
        ApiClient.eventService.loadDetailVote(votingId)
            .enqueue(object : Callback<VoteModel> {
                override fun onResponse(call: Call<VoteModel>, response: Response<VoteModel>) {
                    if(response.isSuccessful) {
                        val body = response.body()
                        val vote = body?.data?.get(0)

                        vote?.let {
                            binding.title.text = it.title
                            binding.text.text = it.description

                            val size = it.voteOptionList.size


                            for(i in 0 until size) {
                                val itemBinding = ItemVoteListBinding.inflate(layoutInflater, null, false)
                                itemBinding.item.text = it.voteOptionList[i].optionTitle
                                binding.layout.addView(itemBinding.root)
                                Log.d("tag", i.toString())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }
}