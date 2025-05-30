package com.start.STart.ui.home.setting.updatehistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.start.STart.databinding.ActivityUpdateHistoryBinding

class UpdateHistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdateHistoryBinding.inflate(layoutInflater) }
    private val updateHistoryAdapter by lazy { UpdateHistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.textTitle.text = "업데이트 내역"
        binding.toolbar.btnBack.setOnClickListener { finish() }
    }

    private fun initRecyclerView() {
        binding.rvUpdateHistory.adapter = updateHistoryAdapter.apply {
            list = listOf(
                UpdateHistory("1.0.1", "2022.09.07", listOf(
                    "ST'art 총학생회 어플리케이션 출시"
                )),
                UpdateHistory("1.0.2", "2022.09.20", listOf(
                    "오타 및 버그 수정", "UI/UX 개선"
                )),
                UpdateHistory("1.0.3", "2022.09.20", listOf(
                    "상시사업 현황이 제대로 보이지 않는 문제 수정"
                )),
                UpdateHistory("1.1.0", "2023.05.06", listOf(
                    "Dream 총학생회 어플리케이션 업데이트",
                )),
                UpdateHistory("1.1.1", "2023.05.06", listOf(
                    "UI/UX 개선 및 버그 수정"
                )),
                UpdateHistory("1.2.0", "2023.05.06", listOf(
                    "투표 기능 추가"
                )),
                UpdateHistory("1.2.1-1.2.2", "2023.05.10", listOf(
                    "버그 수정"
                )),
            )
        }

        binding.rvUpdateHistory.addItemDecoration(DividerItemDecoration(this, 1))
    }
}