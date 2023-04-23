package com.start.STart.ui.home.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.AnswerRequest
import com.start.STart.api.banner.AnswerResponse
import com.start.STart.api.banner.Question
import com.start.STart.api.banner.QuestionModel
import com.start.STart.api.banner.UserStatusModel
import com.start.STart.api.member.response.MemberData
import com.start.STart.databinding.ActivityEscapeBinding
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EscapeActivity : AppCompatActivity() {
    
    val binding by lazy { ActivityEscapeBinding.inflate(layoutInflater)}
    val memberData = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
    var roomId = 0
    var list = mutableListOf<Question>()

    lateinit var requestBody: AnswerRequest

    private val escapeEndDialog by lazy { EscapeEndDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        val isFirst = intent.getBooleanExtra("isFirst", false)
        Log.d("tag", isFirst.toString())

        if(list.isEmpty()) {
            loadQuestion()
        }else {
            loadStatus()
        }


        binding.btnNext.setOnClickListener {

            requestBody = AnswerRequest(memberData!!.studentNo, roomId+1, binding.answer.text.toString())
            Log.d("tag", requestBody.toString())
            loadAnswer(requestBody)

        }

    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadQuestion() {
        ApiClient.eventService.loadQuestion()
            .enqueue(object : Callback<QuestionModel> {
                override fun onResponse(call: Call<QuestionModel>, response: Response<QuestionModel>) {
                    if(response.isSuccessful) {
                        val body = response?.body()

                        val size = body?.data?.size

                        for(i in 0 until size!!) {
                            val tmp = Question(body?.data[i].roomId, body?.data[i].imageUrl)
                            list.add(tmp)
                        }

                        Log.d("tag", list.toString())
                        Log.d("tag", "list 크기 : " + list.size)

                        loadStatus()
                    }
                }

                override fun onFailure(call: Call<QuestionModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })

    }

    private fun loadStatus() {
        ApiClient.eventService.loadStatus()
            .enqueue(object : Callback<UserStatusModel> {
                override fun onResponse(call: Call<UserStatusModel>, response: Response<UserStatusModel>) {
                    if(response.isSuccessful) {
                        roomId = response.body()?.data?.get(0)!!.roomId
                        Log.d("tag", "위치 : $roomId")

                        if(roomId == 8) {
                            binding.btnNext.text = "제출완료"
                            binding.btnNext.isEnabled = false

                            if(!escapeEndDialog.isAdded) {
                                escapeEndDialog.show(supportFragmentManager, ".EscapeEndDialog")
                            }
                        } else {
                            setView()
                        }

                    }
                }

                override fun onFailure(call: Call<UserStatusModel>, t: Throwable) {
                    Log.d("tag", t.message.toString())
                }
            })
    }


    private fun loadAnswer(request: AnswerRequest) {
        ApiClient.eventService.loadAnswer(request)
            .enqueue(object : Callback<AnswerResponse> {
                override fun onResponse(call: Call<AnswerResponse>, response: Response<AnswerResponse>) {
                    if(response.isSuccessful) {

                        val body = response.body()?.data?.get(0)
                        Log.d("tag", body?.answer.toString())

                        // 정답
                        if(body?.answer!!) {
                            if(roomId >=7) {
                                if(!escapeEndDialog.isAdded) {
                                    escapeEndDialog.show(supportFragmentManager, ".EscapeEndDialog")
                                }


                            } else {
                                roomId++
                                startActivity(Intent(applicationContext, EscapeActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    intent.putExtra("isFirst", false)
                                })
                            }
                        } else {
                            // 정답 아닐 때
                            binding.chk.visibility = View.VISIBLE
                        }


                    }
                }

                override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                    Log.d("tag", "answer 오류 : " + t.message.toString())
                }
            })

    }

    private fun setView() {
        Log.d("tag", "load Status List : " + list.size)
        Log.d("tag", "load Status 위치 : "+roomId)
        binding.progressText.text = "총 8문제 중 " + (roomId + 1) + "문제"

        Glide.with(binding.questionImage.context)
            .load(list[roomId].imageUrl)
            .centerCrop()
            .into(binding.questionImage)

        binding.progressBar.max = 8
        binding.progressBar.progress = (roomId + 1)

        if(roomId == 7) {
            binding.btnNext.text = "제출하기"
            binding.progressBar.progress = 0
            binding.progressBar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.background_progress_full)
        }


        binding.answer.addTextChangedListener {
            binding.btnNext.isEnabled = it.toString().isNotBlank()

        }
    }

}