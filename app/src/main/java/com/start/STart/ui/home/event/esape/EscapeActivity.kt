package com.start.STart.ui.home.event.esape

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
import com.start.STart.databinding.ActivityEscapeBinding
import com.start.STart.ui.home.PhotoViewDialog
import com.start.STart.util.showErrorToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EscapeActivity : AppCompatActivity() {
    
    private val binding by lazy { ActivityEscapeBinding.inflate(layoutInflater) }
    private val photoViewDialog by lazy { PhotoViewDialog() }
    private val escapeEndDialog by lazy { EscapeEndDialog() }

    private var escapedRoomIdx = 0
    private var questionList = mutableListOf<Question>()

    private val isCleared
        get() = escapedRoomIdx == questionList.size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initToolbar()

        if(questionList.isEmpty()) {
            loadQuestion()
        }

        binding.inputAnswer.addTextChangedListener {
            binding.btnNext.isEnabled = it.toString().isNotBlank()
        }

        binding.btnNext.setOnClickListener {
            val answer = binding.inputAnswer.text.toString()
            loadAnswer(AnswerRequest(
                escapedRoomIdx + 1,
                answer
            ))
        }

        binding.questionImage.setOnClickListener {
            photoViewDialog.show(this, url = questionList[escapedRoomIdx].imageUrl)
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun loadQuestion() {
        ApiClient.eventService.loadQuestion()
            .enqueue(object : Callback<QuestionModel> {

                override fun onResponse(call: Call<QuestionModel>, response: Response<QuestionModel>) {
                    if(response.isSuccessful) {
                        val questionModel = response.body()

                        questionModel?.data?.forEach {
                            questionList.add(it)
                        }
                        loadStatus()
                    } else {
                        showErrorToast(this@EscapeActivity, "문제를 불러올 수 없습니다.")
                    }
                }

                override fun onFailure(call: Call<QuestionModel>, t: Throwable) {
                    showErrorToast(this@EscapeActivity, "문제를 불러올 수 없습니다.")
                }
            })

    }

    private fun loadStatus() {
        ApiClient.eventService.loadStatus()
            .enqueue(object : Callback<UserStatusModel> {

                override fun onResponse(call: Call<UserStatusModel>, response: Response<UserStatusModel>) {
                    if(response.isSuccessful) {

                        escapedRoomIdx = response.body()?.data?.get(0)!!.roomId

                        if(isCleared) {
                            binding.btnNext.text = "제출완료"
                            binding.btnNext.isEnabled = false

                            escapeEndDialog.show(this@EscapeActivity)
                        } else {
                            updateUI()
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
                        val data = response.body()?.data?.get(0)
                        if(data?.answer == true) {
                            loadStatus()
                        } else {
                            binding.textFail.visibility = View.VISIBLE
                        }
                    } else {
                        showErrorToast(this@EscapeActivity)
                    }
                }

                override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                    showErrorToast(this@EscapeActivity)
                }
            })

    }

    private val isLastQuestion
        get() = escapedRoomIdx == questionList.size - 1

    private fun updateUI() {
        binding.progressText.text = "총 ${questionList.size}문제 중 " + (escapedRoomIdx + 1) + "문제"
        binding.inputAnswer.setText("")
        binding.textFail.visibility = View.INVISIBLE

        Glide.with(this)
            .load(questionList[escapedRoomIdx].imageUrl)
            .into(binding.questionImage)

        binding.progressBar.max = questionList.size
        binding.progressBar.progress = (escapedRoomIdx + 1)

        if (isLastQuestion) {
            binding.btnNext.text = "제출하기"
            binding.progressBar.progress = 0
            binding.progressBar.progressDrawable =
                ContextCompat.getDrawable(this, R.drawable.background_progress_full)
        }
    }
}