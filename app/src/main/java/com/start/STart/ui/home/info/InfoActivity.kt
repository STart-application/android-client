package com.start.STart.ui.home.info

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.start.STart.R
import com.start.STart.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityInfoBinding.inflate(layoutInflater) }

    val tabList = listOf(
        Info1Fragment(),
        Info2Fragment(),
        Info3Fragment(),
    )
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, tabList[0])
            .commit()


        binding.txt1.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[0])
                .commit()

            binding.txt1.setTextColor(ContextCompat.getColor(this, R.color.dream_purple))
            binding.txt2.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
            binding.txt3.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn1.layoutParams.height = (resources.displayMetrics.density * 5).toInt()
            binding.btn1.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple))

            binding.btn2.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn2.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn3.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn3.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))



        }

        binding.txt2.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[1])
                .commit()

            binding.txt1.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
            binding.txt2.setTextColor(ContextCompat.getColor(this, R.color.dream_purple))
            binding.txt3.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn1.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn1.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn2.layoutParams.height = (resources.displayMetrics.density * 5).toInt()
            binding.btn2.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple))

            binding.btn3.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn3.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn1.requestLayout()
            binding.btn2.requestLayout()
            binding.btn3.requestLayout()


        }

        binding.txt3.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, tabList[2])
                .commit()

            binding.txt1.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
            binding.txt2.setTextColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))
            binding.txt3.setTextColor(ContextCompat.getColor(this, R.color.dream_purple))

            binding.btn1.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn1.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn2.layoutParams.height = (resources.displayMetrics.density * 3).toInt()
            binding.btn2.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple_ghost))

            binding.btn3.layoutParams.height = (resources.displayMetrics.density * 5).toInt()
            binding.btn3.setBackgroundColor(ContextCompat.getColor(this, R.color.dream_purple))

            binding.btn1.requestLayout()
            binding.btn2.requestLayout()
            binding.btn3.requestLayout()


        }
    }
}