package com.start.STart.ui.home.info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.start.STart.R
import com.start.STart.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInfoBinding.inflate(layoutInflater) }

    val tabList = listOf(
        Info1Fragment(),
        Info2Fragment(),
        Info3Fragment(),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, tabList[0])
            .commit()

        binding.btn1.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[0])
                .commit()
        }

        binding.btn2.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[1])
                .commit()
        }

        binding.btn3.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[2])
                .commit()
        }
    }
}