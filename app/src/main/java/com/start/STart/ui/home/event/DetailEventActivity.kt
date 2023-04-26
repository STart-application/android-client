package com.start.STart.ui.home.event

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.start.STart.R
import com.start.STart.api.banner.Event
import com.start.STart.databinding.ActivityDetailEventBinding
import com.start.STart.ui.auth.util.AuthenticationUtil
import com.start.STart.ui.home.event.esape.EscapeActivity
import com.start.STart.ui.home.event.vote.VoteActivity
import com.start.STart.util.Constants
import com.start.STart.util.getParcelableExtra
import com.start.STart.util.showErrorToast

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
                    Constants.EVENT_CODE_VOTE, Constants.EVENT_CODE_ESCAPE -> {
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
                    Constants.EVENT_CODE_VOTE, Constants.EVENT_CODE_ESCAPE -> {
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
                Constants.EVENT_CODE_VOTE -> {
                    AuthenticationUtil.performActionOnLogin({
                        startActivity(Intent(applicationContext, VoteActivity::class.java))
                    }, failListener = {
                        showErrorToast(this, it.loginFailMessage)
                    })
                }

                Constants.EVENT_CODE_ESCAPE -> {
                    AuthenticationUtil.performActionOnLogin({
                        startActivity(Intent(applicationContext, EscapeActivity::class.java).apply {
                            putExtra(Constants.EXTRA_FLAG_FIRST_ESCAPE_ROOM, true)
                        })
                    }, failListener = {
                        showErrorToast(this, it.loginFailMessage)
                    })
                }
                else -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.formLink)))
                }
            }
        }


    }

}