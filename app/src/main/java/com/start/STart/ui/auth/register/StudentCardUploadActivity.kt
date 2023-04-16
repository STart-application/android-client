package com.start.STart.ui.auth.register

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.skydoves.balloon.Balloon
import com.start.STart.BuildConfig
import com.start.STart.R
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.ActivityValidateStudentCardBinding
import com.start.STart.util.Constants
import com.start.STart.util.getParcelableExtra
import com.start.STart.util.getPart
import es.dmoral.toasty.Toasty
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
    private lateinit var cacheUri: Uri
    private lateinit var studentCardUri: Uri

    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .into(binding.inputStudentCard)
            studentCardUri = it
            binding.btnNext.isEnabled = true
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if(isSuccessful) {
            studentCardUri = cacheUri
            studentCardUri.let {
                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .into(binding.inputStudentCard)
                studentCardUri = it
                binding.btnNext.isEnabled = true
            }
        } else {

        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted) {
            startCamera()
        } else {
            Toasty.error(this, "카메라 궈한을 허용해주세요.").show()
        }
    }

    private val isCameraGranted get() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initView()

    }

    fun startCamera() {
        if(isCameraGranted) {
            cacheUri = createCacheFile()
            cameraLauncher.launch(cacheUri)
        } else {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
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
            selectPhotoDialog.show(supportFragmentManager, null)
        }
        binding.btnNext.setOnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            binding.btnNext.isEnabled = false

            lifecycleScope.launch(Dispatchers.IO) {
                val part = getPart(applicationContext, studentCardUri)
                viewModel.register(registerData, part)
            }
        }
    }

    private fun initViewModelListeners() {
        viewModel.registerResult.observe(this) { result ->
            if (result.isSuccessful) {
                startActivity(Intent(this, RegisterCompleteActivity::class.java))
            } else {
                Toasty.error(this, result.message!!).show()
            }

            binding.progressbar.visibility = View.GONE
            binding.btnNext.isEnabled = true
        }
    }

    private fun createCacheFile(): Uri {
        val file = File.createTempFile("student_card", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", file)
    }
}