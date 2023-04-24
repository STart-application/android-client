package com.start.STart.ui.home.setting.suggest

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.start.STart.R
import com.start.STart.api.suggestion.request.SuggestRequest
import com.start.STart.databinding.ActivitySuggestBinding
import com.start.STart.util.getPart
import com.start.STart.util.px2dp
import com.start.STart.util.showErrorToast
import com.start.STart.util.showSuccessToast

class SuggestActivity : AppCompatActivity() {

    companion object {

        const val KEY_TYPE_SUGGEST = "key_type_suggest"

        const val TYPE_FEATURE = "FEATURE"
        const val TYPE_ERROR = "ERROR"
        const val TYPE_ETC = "ETC"
    }

    private lateinit var type: String

    private val binding by lazy { ActivitySuggestBinding.inflate(layoutInflater) }
    private val viewModel: SuggestViewModel by viewModels()

    private var imageUploadState = false
    private var uploadedImage: Uri? = null
    private val imageUploadLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uploadedImage = uri
        imageUploadState = uri != null
        updateUploadButton()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
        initViewListeners()
        initViewModelListeners()
    }

    private fun init() {
        type = intent?.getStringExtra(KEY_TYPE_SUGGEST) ?: ""
    }

    private fun initView() {
        binding.toolbar.textTitle.text = when(type) {
            TYPE_FEATURE -> "기능 개선 제안"
            TYPE_ERROR -> "오류 신고"
            TYPE_ETC -> "기타 제안"
            else -> ""
        }
        initUploadButton()
    }

    private fun initUploadButton() {
        binding.btnAddImage.setOnClickListener {
            if(!imageUploadState) {
                imageUploadLauncher.launch("image/*")
            }
        }

        binding.btnDelete.setOnClickListener {
            uploadedImage = null

            imageUploadState = false
            updateUploadButton()
        }
    }

    private fun initViewListeners() {
        binding.btnSend.setOnClickListener {
            if(isInputValid) {
                val request = SuggestRequest(
                    binding.inputTitle.text.toString(),
                    binding.inputContent.text.toString(),
                    type,
                    getPart(this, uploadedImage)
                )

                viewModel.sendSuggestion(request)
            } else {
                Balloon.Builder(this)
                    .setText("모든 입력칸을 채워주세요!")
                    .setPadding(8)
                    .setWidth(px2dp(binding.btnSend.width.toFloat()).toInt())
                    .setBalloonAnimation(BalloonAnimation.ELASTIC)
                    .setAutoDismissDuration(1000L)
                    .setBackgroundColor(resources.getColor(R.color.dream_purple))
                    .build()
                    .showAlignTop(binding.btnSend)

            }
        }
        binding.btnCancel.setOnClickListener { finish() }
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewModelListeners() {
        viewModel.suggestionResult.observe(this) {
            if(it.isSuccessful) {
                showSuccessToast(this, "성공적으로 전달하였습니다!")
                finish()
            } else {
                showErrorToast(this, it.message)
            }
        }
    }

    private fun updateUploadButton() {

        binding.btnAddImage.setBackgroundResource(if(imageUploadState) R.drawable.background_suggest_add_image else R.drawable.background_round_22_5)

        binding.btnDelete.visibility = if(imageUploadState) View.VISIBLE else View.GONE


        val color = resources.getColor(if(imageUploadState) R.color.dream_purple else R.color.text_caption)
        binding.imagePhoto.setColorFilter(color)
        binding.textAddImage.setTextColor(color)

        binding.textAddImage.text = if(imageUploadState) "사진 추가됨" else "사진 추가"

    }

    private val isInputValid: Boolean
        get() = binding.inputTitle.text.toString().isNotBlank() &&
                binding.inputContent.text.toString().isNotBlank()
}