package com.start.STart.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.FileProvider
import com.start.STart.BuildConfig
import com.start.STart.databinding.ActivityPolicyBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class PolicyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPolicyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        initToolbar()
        initPolicyCustomTab()
        initCheckBox()

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, StudentInfoInputActivity::class.java))
        }
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "약관동의"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initPolicyCustomTab() {
        binding.textMorePrivacy.setOnClickListener {
            //openCustomTab(resources.getString(R.string.link_privacy_policy))
            openPdf("sc_privacy.pdf")
        }

        binding.textMoreService.setOnClickListener {
            //openCustomTab(resources.getString(R.string.link_terms_of_service))
            openPdf("sc_service.pdf")
        }
    }

    private fun initCheckBox() {
        val checkBoxList = listOf(binding.checkPrivacyPolicy, binding.checkService)

        binding.checkAll.setOnClickListener {
            updateSubCheckBox(checkBoxList, binding.checkAll.isChecked)
        }

        binding.checkAll.setOnCheckedChangeListener { _, isChecked ->
            binding.btnNext.isEnabled = isChecked
        }

        initSubCheckBox(checkBoxList)
    }

    private fun initSubCheckBox(checkBoxList: List<AppCompatCheckBox>) {
        checkBoxList.forEach {
            it.setOnCheckedChangeListener { _, _ ->
                if (!it.isChecked) {
                    binding.checkAll.isChecked = false
                } else {
                    val allChecked = checkBoxList.all { it.isChecked }
                    binding.checkAll.isChecked = allChecked
                }
            }
        }
    }

    private fun updateSubCheckBox(checkBoxList: List<CheckBox>, isChecked: Boolean) {
        binding.checkAll.isChecked = isChecked
        checkBoxList.forEach { it.isChecked = isChecked }
    }

    private fun openPdf(fileName: String) {
        copyFileFromAssets(fileName)
        val file = File("$filesDir/$fileName")

        var uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyFileFromAssets(fileName: String) {
        val assetManager = this.assets

        val cacheFile = File("$filesDir/$fileName")
        var in1: InputStream? = null
        var out: OutputStream? = null
        try {
            if (cacheFile.exists()) {
                return
            } else {
                in1 = assetManager.open(fileName)
                out = FileOutputStream(cacheFile)
                copyFile(in1, out)
                in1.close()
                out.flush()
                out.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyFile(in1: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (in1.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }

}