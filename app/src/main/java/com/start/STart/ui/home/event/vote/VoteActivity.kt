package com.start.STart.ui.home.event.vote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.start.STart.api.ApiClient
import com.start.STart.api.event.vote.VoteModel
import com.start.STart.databinding.ActivityVoteBinding
import com.start.STart.util.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VoteActivity : AppCompatActivity() {

    val binding by lazy {ActivityVoteBinding.inflate(layoutInflater)}
    private val voteAdapter by lazy { VoteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvEvent.adapter = voteAdapter
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        loadVote()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }
    private fun loadVote() {
        ApiClient.eventService.loadVoteList()
            .enqueue(object : Callback<VoteModel> {
                override fun onResponse(call: Call<VoteModel>, response: Response<VoteModel>) {
                    if(response.isSuccessful) {
                        val list = response.body()?.data
                        list?.let {
                            voteAdapter.list = it
                            voteAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    showErrorToast(this@VoteActivity)
                }
            })
    }

}