package com.start.STart.ui.home.event.vote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.Vote
import com.start.STart.api.banner.VoteModel
import com.start.STart.databinding.ActivityDetailVoteBinding
<<<<<<< HEAD:app/src/main/java/com/start/STart/ui/home/event/vote/DetailVoteActivity.kt
import com.start.STart.ui.home.event.vote.VoteListAdapter
=======
import com.start.STart.databinding.ItemVoteBinding
import com.start.STart.databinding.ItemVoteListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
>>>>>>> event:app/src/main/java/com/start/STart/ui/home/event/DetailVoteActivity.kt

class DetailVoteActivity : AppCompatActivity() {
    val binding by lazy {ActivityDetailVoteBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        var votingId = intent.getIntExtra("vote", 0)
        Log.d("tag", votingId.toString())
        loadDetailVote(votingId)
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadDetailVote(votingId: Int) {
        ApiClient.eventService.loadDetailVote()
            .enqueue(object : Callback<VoteModel> {
                override fun onResponse(call: Call<VoteModel>, response: Response<VoteModel>) {
                    if(response.isSuccessful) {
                        val body = response?.body()
                        /*
                        binding.title.text = body?.title
                        binding.text.text = body?.description

                        val size = body?.voteOptionList?.size
                        val itemBinding = ItemVoteListBinding.inflate(layoutInflater)

                        for(i in 0 until size!!) {
                            itemBinding.item.text = body.voteOptionList[i].optionTitle
                            binding.layout.addView(itemBinding.root)
                            Log.d("tag", i.toString())
                        }


                         */
                    }


                }

                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }

}