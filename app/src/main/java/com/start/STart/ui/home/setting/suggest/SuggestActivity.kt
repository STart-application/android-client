package com.start.STart.ui.home.setting.suggest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.start.STart.R
import com.start.STart.databinding.ActivitySuggestBinding

class SuggestActivity : AppCompatActivity() {

    companion object {

        const val TYPE_SUGGEST = "type_suggest"

        const val TYPE_FEATURE = "feature"
        const val TYPE_ERROR = "error"
        const val TYPE_ETC = "etc"
    }

    private lateinit var type: String

    private val binding by lazy { ActivitySuggestBinding.inflate(layoutInflater) }
    private val viewModel: SuggestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewListeners()
    }

    private fun init() {
        type = intent?.getStringExtra(TYPE_SUGGEST) ?: ""
    }

    private fun initView() {
        binding.toolbar.textTitle.text = when(type) {
            TYPE_FEATURE -> "기능 개선 제안"
            TYPE_ERROR -> "오류 신고"
            TYPE_ETC -> "기타 제안"
            else -> ""
        }
        
        var state = false
        binding.btnAddImage.setOnClickListener {
            state = !state
            onImageStateChanged(state)
        }
    }

    private fun initViewListeners() {
        binding.btnSend.setOnClickListener {
            if(isInputValid) {
                viewModel.sendSuggestion()
            } else {
                Balloon.Builder(this)
                    .setText("모든 입력칸을 채워주세요!")
                    .setPadding(8)
                    .setBackgroundColor(resources.getColor(R.color.dream_purple))
                    .build()
                    .showAlignTop(binding.btnSend)
            }
        }
        binding.btnCancel.setOnClickListener { finish() }
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun onImageStateChanged(added: Boolean) {
        binding.btnAddImage.setBackgroundResource(if(added) R.drawable.background_suggest_add_image else R.drawable.background_round_22_5)
        val color = resources.getColor(if(added) R.color.dream_purple else R.color.dream_gray)
        binding.imagePhoto.setColorFilter(color)
        binding.textAddImage.setTextColor(color)
        binding.textAddImage.text = if(added) "사진 추가됨" else "사진 추가"

    }

    private val isInputValid: Boolean
        get() = binding.inputTitle.text.toString().isNotBlank() &&
                binding.inputContent.text.toString().isNotBlank()
}