package com.start.STart.ui.home.setting.suggest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
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
    }
}