package com.start.STart.ui.home.festival.info.contents

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.start.STart.R
import com.start.STart.api.festival.response.BoothData
import com.start.STart.util.dp2px

class ContentsFragment : Fragment() {

    private val contentsAdapter by lazy { ContentsAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentsRecyclerView = view.findViewById<RecyclerView>(R.id.contenstRecyclerView)
        contentsRecyclerView.adapter = contentsAdapter.also {
            it.list = listOf(
                BoothData(1, "대형 캔버스", 1),
                BoothData(2, "대형 캔버스", 2),
                BoothData(3, "대형 캔버스", 3),
                BoothData(4, "대형 캔버스", 1),
                BoothData(5, "대형 캔버스", 2),
            )
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contents, container, false)
    }

}