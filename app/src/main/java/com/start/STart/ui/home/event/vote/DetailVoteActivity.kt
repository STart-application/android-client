package com.start.STart.ui.home.event.vote

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.start.STart.R
import com.start.STart.api.ApiClient
import com.start.STart.api.event.vote.VoteModel
import com.start.STart.api.event.vote.VoteOption
import com.start.STart.api.event.vote.VoteRequest
import com.start.STart.databinding.ActivityDetailVoteBinding
import com.start.STart.databinding.ItemVoteFin2Binding
import com.start.STart.databinding.ItemVoteFinBinding
import com.start.STart.databinding.ItemVoteListBinding
import com.start.STart.util.showErrorToast
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailVoteActivity : AppCompatActivity() {
    val binding by lazy { ActivityDetailVoteBinding.inflate(layoutInflater) }

    var votingId = 0
    var max = 0
    var min = 0
    var count = 0
    var request: VoteRequest? = VoteRequest(0, mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        votingId = intent.getIntExtra("vote", 0)
        request?.votingId = votingId

        val isEmpty = intent.getBooleanExtra("vote1", true)

        if(isEmpty) {
            loadDetailVote(votingId)
        } else {
            setViewFin()
        }

        binding.button.setOnClickListener {
            if(binding.button.text == "투표완료") {
                Toasty.success(applicationContext, "투표 완료", Toast.LENGTH_SHORT).show()
            } else {
                postVote(VoteRequest(request?.votingId!!, request?.votingOptionIds!!))
            }

        }
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
                        val body = response.body()?.data?.get(0)

                        binding.title.text = body?.title
                        binding.text.text = body?.description

                        val size = body?.voteOptionList?.size!!

                        max = body.maxSelect!!
                        min = body.minSelect!!
                        count = 0

                        for(i in 0 until size) {
                            addItem(body.voteOptionList[i], binding.layout)
                        }
                    }

                }

                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }

    val checkBoxClickListener = View.OnClickListener { view ->
        val checkBox = view as CheckBox

        if(checkBox.isChecked) {
            count++
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setTextAppearance(R.style.Theme_Dream_Typeface_Body_1_Bold)
                checkBox.setTextColor(ContextCompat.getColor(this@DetailVoteActivity, R.color.dream_purple))
                request?.votingOptionIds?.add(checkBox.id)
            }

            if(count > max) {
                Toasty.info(applicationContext, "정해진 개수보다 많이 선택했습니다", Toast.LENGTH_SHORT).show()
                checkBox.isChecked = false
                count--
                request?.votingOptionIds?.remove(checkBox.id)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkBox.setTextAppearance(R.style.Theme_Dream_Typeface_Body_1)
                }

            }
        } else {
            count--
            request?.votingOptionIds?.remove(checkBox.id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkBox.setTextAppearance(R.style.Theme_Dream_Typeface_Body_1)
            }

        }
    }

    private fun postVote(voteRequest: VoteRequest) {

        ApiClient.eventService.postVote(voteRequest)
            .enqueue(object : Callback<VoteModel> {
                override fun onResponse(call: Call<VoteModel>, response: Response<VoteModel>) {
                    if(response.isSuccessful) {
                        binding.layout.removeAllViews()
                        setViewFin()
                    } else {
                        val errorBody = ApiClient.parseBody(response.errorBody()?.string())
                        Log.d("tag", "${errorBody?.message}")
                        showErrorToast(this@DetailVoteActivity, errorBody?.message)
                    }
                }

                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }

    private fun addItem(option: VoteOption, layout: LinearLayout) {
        val itemBinding = ItemVoteListBinding.inflate(LayoutInflater.from(this), null, false)
        itemBinding.item.text = option.optionTitle
        itemBinding.item.id = option.votingOptionId
        itemBinding.item.setOnClickListener(checkBoxClickListener)

        layout.addView(itemBinding.root)
    }

    private fun addItem2(option: VoteOption, layout: LinearLayout) {
        val itemFinBinding = ItemVoteFinBinding.inflate(LayoutInflater.from(this), null, false)
        itemFinBinding.item.text = option.optionTitle
        itemFinBinding.item.id = option.votingOptionId

        layout.addView(itemFinBinding.root)
    }

    private fun setViewFin() {
        binding.layout.removeAllViews()

        ApiClient.eventService.loadDetailVote(votingId)
            .enqueue(object : Callback<VoteModel> {
                override fun onResponse(call: Call<VoteModel>, response: Response<VoteModel>) {
                    if(response.isSuccessful) {
                        val body = response?.body()?.data?.get(0)

                        binding.title.text = body?.title
                        binding.text.text = body?.description

                        val size = body?.voteOptionList?.size!!

                        for(i in 0 until size!!) {
                            if (body.userSelectedOptionIds.contains(body.voteOptionList[i].votingOptionId)) {
                                val itemFin2Binding = ItemVoteFin2Binding.inflate(LayoutInflater.from(applicationContext), null, false)
                                itemFin2Binding.item.text = body.voteOptionList[i].optionTitle
                                itemFin2Binding.item.id = body.voteOptionList[i].votingOptionId
                                binding.layout.addView(itemFin2Binding.root)
                            } else {
                                addItem2(body.voteOptionList[i], binding.layout)

                            }
                        }
                    }
                }
                override fun onFailure(call: Call<VoteModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })


        binding.button.text = "투표완료"
        val ghost = ContextCompat.getColor(binding.button.context, R.color.dream_purple_ghost)
        binding.button.backgroundTintList = ColorStateList.valueOf(ghost)
    }
}