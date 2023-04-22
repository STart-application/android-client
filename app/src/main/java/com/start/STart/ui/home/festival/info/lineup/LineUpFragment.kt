package com.start.STart.ui.home.festival.info.lineup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.start.STart.api.festival.response.FestivalInfoData
import com.start.STart.api.festival.response.LineUpData
import com.start.STart.databinding.FragmentLineUpBinding
import com.start.STart.ui.home.festival.info.FestivalInfoViewModel
import com.start.STart.util.showErrorToast

class LineUpFragment : Fragment() {

    companion object {
        const val MAX_SIZE = 3
    }

    private var _binding: FragmentLineUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(FestivalInfoViewModel::class.java)
    }

    private val lineUpViewPagerAdapter by lazy { LineUpViewPagerAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initRecyclerView()
        initViewPager()
        initLiveDataObservers()
    }

    private fun initViewPager() {
        binding.timeLineViewPager.adapter = lineUpViewPagerAdapter

        binding.timeLineViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_IDLE) {
                    updateViewPager()
                }
            }
        })

        binding.btnPrevious.setOnClickListener {
            if(binding.timeLineViewPager.currentItem > 0) {
                binding.timeLineViewPager.currentItem -= 1
            }
        }

        binding.btnNext.setOnClickListener {
            if(binding.timeLineViewPager.currentItem < MAX_SIZE) {
                binding.timeLineViewPager.currentItem += 1
            }
        }
    }

    private fun updateViewPager() {
        val pair = lineUpViewPagerAdapter.list[binding.timeLineViewPager.currentItem]
        binding.textDate.text = pair.first
    }

    private fun initRecyclerView() {
        //binding.timeLineRecyclerView.adapter = lineUpAdapter
    }


    private fun initLiveDataObservers() {


        viewModel.loadFestivalInfoResult.observe(viewLifecycleOwner) { result ->
            if(result.isSuccessful) {
                val lineUpList = (result.data as List<FestivalInfoData>)[0].lineUpList
//                val map = lineUpList.associateBy {
//                    it.lineUpDay
//                }

                val map = mutableMapOf<String, MutableList<LineUpData>>().apply {
                    lineUpList.forEach { lineup ->
                        getOrPut(lineup.lineUpDay) { mutableListOf() }.add(lineup)
                    }
                }
                lineUpViewPagerAdapter.list = map.toList()
                updateViewPager()
                Log.d(null, "initLiveDataObservers: $map")
            } else {
                showErrorToast(requireContext(), result.message!!)
            }
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