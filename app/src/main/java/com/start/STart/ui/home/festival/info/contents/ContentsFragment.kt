package com.start.STart.ui.home.festival.info.contents

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.api.festival.response.FestivalInfoData
import com.start.STart.ui.home.festival.info.FestivalInfoViewModel
import com.start.STart.util.dp2px

class ContentsFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(FestivalInfoViewModel::class.java)
    }

    private lateinit var contentsRecyclerView: RecyclerView

    private val contentsAdapter by lazy { ContentsAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentsRecyclerView = view.findViewById(R.id.contenstRecyclerView)
        initRecyclerView()

        initLiveDataResults()
    }

    private fun initRecyclerView() {
        contentsRecyclerView.adapter = contentsAdapter.also {
            it.list = BoothEnum.values().toList()
        }
        contentsRecyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val spacing = view.context.dp2px(20f).toInt()

                val position = parent.getChildAdapterPosition(view)

                if (position >= 1) {
                    outRect.top = spacing
                }
            }
        })
    }

    private fun initLiveDataResults() {
        viewModel.loadFestivalInfoResult.observe(viewLifecycleOwner) { result ->
            if(result.isSuccessful) {
                val boothList = (result.data as List<FestivalInfoData>)[0].boothList
                boothList.forEach { boothData ->
                    val booth = BoothEnum.values().getOrNull(boothData.boothId - 1)
                    if(booth != null) {
                        booth.congestion = boothData.congestion
                    }
                }
                contentsAdapter.notifyDataSetChanged()
            }
            // LineUpFragment 실패 토스트 띄움
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contents, container, false)
    }

}