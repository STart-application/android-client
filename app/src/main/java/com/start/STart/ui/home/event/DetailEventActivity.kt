package com.start.STart.ui.home.event

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.start.STart.R
import com.start.STart.api.banner.Event
import com.start.STart.databinding.ActivityDetailEventBinding
import com.start.STart.ui.auth.util.AuthenticationUtil
import com.start.STart.ui.home.PhotoViewDialog
import com.start.STart.ui.home.event.esape.EscapeActivity
import com.start.STart.ui.home.event.vote.VoteActivity
import com.start.STart.util.Constants
import com.start.STart.util.getParcelableExtra
import com.start.STart.util.openCustomTab
import com.start.STart.util.showErrorToast

class DetailEventActivity : TransformationAppCompatActivity() {

    private val binding by lazy { ActivityDetailEventBinding.inflate(layoutInflater) }

    private val photoViewDialog by lazy { PhotoViewDialog() }

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
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initView(event: Event) {


        Glide.with(this)
            .load(event.imageUrl)
            .into(binding.cardImage)

        binding.cardImage.setOnClickListener {
            photoViewDialog.show(this, url = event.imageUrl)
        }

        val grayColor = ContextCompat.getColor(this, R.color.text_caption)
        val eventStatus = EventStatus.valueOf(event.eventStatus)

        binding.eventTitle.text = event.title

        val primaryColor = ContextCompat.getColor(this, eventStatus.titleColor)
        binding.eventTitle.setTextColor(primaryColor)
        binding.divider.setBackgroundColor(primaryColor)

        binding.btnConfirm.also {
            it.isEnabled = eventStatus.buttonEnabled
            when (event.eventId) {
                Constants.EVENT_CODE_VOTE -> {
                    it.text = "참여하기"
                    it.setOnClickListener {
                        AuthenticationUtil.performActionOnLogin({
                            startActivity(Intent(applicationContext, VoteActivity::class.java))
                        }, failListener = { authUtil ->
                            showErrorToast(this, authUtil.loginFailMessage)
                        })
                    }
                }
                Constants.EVENT_CODE_ESCAPE -> {
                    it.text = "참여하기"
                    it.setOnClickListener {
                        AuthenticationUtil.performActionOnLogin({
                            startActivity(Intent(applicationContext, EscapeActivity::class.java).apply {
                                putExtra(Constants.EXTRA_FLAG_FIRST_ESCAPE_ROOM, true)
                            })
                        }, failListener = { authUtil ->
                            showErrorToast(this, authUtil.loginFailMessage)
                        })
                    }
                }
                else -> {
                    if (eventStatus == EventStatus.END) {
                        it.text = "종료된 이벤트"
                        it.backgroundTintList = ColorStateList.valueOf(grayColor)
                    } else {
                        it.text = "신청하기"
                    }

                    it.setOnClickListener {
                        openCustomTab(event.formLink)
                    }
                }
            }
        }
    }

    companion object {
        fun startActivity(
            context: Context,
            transformationLayout: TransformationLayout,
            event: Event
        ) {
            val intent = Intent(context, DetailEventActivity::class.java)
            intent.putExtra("event", event)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }
}