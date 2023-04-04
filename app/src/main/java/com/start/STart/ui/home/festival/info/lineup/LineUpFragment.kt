package com.start.STart.ui.home.festival.info.lineup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.start.STart.R
import com.start.STart.api.festival.response.LineUpData
import com.start.STart.databinding.FragmentLineUpBinding

class LineUpFragment : Fragment() {

    private var _binding: FragmentLineUpBinding? = null
    private val binding get() = _binding!!

    private val lineUpAdapter by lazy { TimeLineAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.timeLineRecyclerView.adapter = lineUpAdapter.apply {
            list = listOf(
                LineUpData(1, "1234", "12한글입니다.한글입니다.한글입니다.한글입니다.4135", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.", "한글입니다.한글입니다.", "sdfsdf"),
                LineUpData(1, "1234", "124135", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.", "124135", "sdfsdf"),
                LineUpData(1, "1234", "한글입니다.한글입니다.한글입니다.한글입니다.", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.", "한글입니다.한글입니다.한글입니다.한글입니다.", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.", "한글입니다.한글입니다.", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.", "한글입니다.한글입니다.", "sdfsdf"),
                LineUpData(1, "한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.한글입니다.", "한글입니다.한글입니다.", "sdfsdf"),
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLineUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}