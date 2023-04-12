package com.start.STart.ui.auth.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.start.STart.R
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateStudentCardBinding
import com.start.STart.util.Constants
import com.start.STart.util.getParcelableExtra
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class StudentCardUploadActivity : AppCompatActivity() {
    private val binding by lazy { ActivityValidateStudentCardBinding.inflate(layoutInflater) }
    private val viewModel: StudentCardUploadViewModel by viewModels()

    private val selectPhotoDialog by lazy { SelectPhotoDialog() }

    private lateinit var registerData: RegisterData
    private lateinit var studentCardUri: Uri

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.inputStudentCard)
            studentCardUri = it
            binding.btnNext.isEnabled = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()
    }

    private fun init() {
        registerData = intent.getParcelableExtra(key = Constants.KEY_REGISTER_DATA)!!
    }

    private fun initView() {
        initToolbar()
        initViewListeners()
        initViewModelListeners()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "학생증 인증"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initViewListeners() {
        binding.layoutInputStudentCard.setOnClickListener {
            //imageLauncher.launch("image/*")
            selectPhotoDialog.show(supportFragmentManager, null)
        }
        binding.btnNext.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val part = getPart(studentCardUri)
                viewModel.register(registerData, part)
            }
        }
    }

    private fun initViewModelListeners() {
        viewModel.registerResult.observe(this) {
            if(it.isSuccessful) {
                startActivity(Intent(this, RegisterCompleteActivity::class.java))
            } else {
                Balloon.Builder(this)
                    .setText(it.message.toString())
                    .setPadding(8)
                    .setBackgroundColor(resources.getColor(R.color.dream_purple))
                    .setArrowPosition(0.5f)
                    .build()
                    .showAlignTop(binding.btnNext)
            }
        }
    }

    private fun getPart(uri: Uri): MultipartBody.Part {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(externalCacheDir, "student_card.jpg")
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }
}