package com.start.STart.ui.home.event

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.banner.Event
import com.start.STart.databinding.ActivityDetailEventBinding
import com.start.STart.util.getParcelableExtra

class DetailEventActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailEventBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initToolbar()

        val event = intent.getParcelableExtra<Event>(key = "event")
        if (event != null) {
            initView(event)
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "이벤트 참여"
        binding.toolbar.btnBack.visibility = View.VISIBLE
        binding.toolbar.icSetting.visibility = View.INVISIBLE
        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initView(event: Event) {
        binding.eventTitle.text = event.title

        Glide.with(binding.cardImage.context)
            .load(event.imageUrl)
            .into(binding.cardImage)

        val status = event.eventStatus

        val purple_ghost = ContextCompat.getColor(binding.button.context, R.color.dream_purple_ghost)
        val purple = ContextCompat.getColor(binding.button.context, R.color.dream_purple)
        val gray = ContextCompat.getColor(binding.button.context, R.color.text_caption)



        when(status) {
            "PROCEEDING" -> {
                binding.button.backgroundTintList = ColorStateList.valueOf(purple)
                when(event.eventId) {
                    999, 998 -> {
                        binding.button.text = "참여하기"
                    }

                    else -> {
                        binding.button.text = "신청하기"
                    }
                }
                binding.eventTitle.setTextColor(purple)
            }

            "BEFORE" -> {
                binding.button.backgroundTintList = ColorStateList.valueOf(purple_ghost)
                when(event.eventId) {
                    999, 998 -> {
                        binding.button.text = "참여하기"
                    }

                    else -> {
                        binding.button.text = "신청하기"
                    }
                }
                binding.button.isEnabled = false

                binding.eventTitle.setTextColor(purple_ghost)
            }

            "END" -> {
                binding.button.backgroundTintList = ColorStateList.valueOf(gray)
                binding.button.text ="종료된 이벤트"
                binding.button.isEnabled = false

                binding.eventTitle.setTextColor(gray)
            }

        }

        binding.button.setOnClickListener {
            when(event.eventId) {
                // 투표
                998 -> {
                    startActivity(Intent(applicationContext, VoteActivity::class.java))
                }

                // 방탈출
                999 -> {
                    val intent = Intent(applicationContext, EscapeActivity::class.java)
                    intent.putExtra("isFirst", true)
                    startActivity(intent)
                }
                else -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.formLink)))
                }
            }
        }


    }

}