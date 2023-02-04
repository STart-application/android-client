package com.start.STart.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.start.STart.R
import com.start.STart.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sliderAdapter by lazy { SliderAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*Glide.with(this)
            .load()
            .into(binding.slider)*/
        binding.slider.offscreenPageLimit = 1
        binding.slider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        binding.slider.adapter = sliderAdapter.apply {
            list = listOf(
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
                "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/307460857_451409030280466_4565862306706252028_n.jpg?stp=dst-jpg_e35&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=110&_nc_ohc=RHMYwcmGWMwAX_8Goax&edm=AOQ1c0wBAAAA&ccb=7-5&oh=00_AfC2Sr_hDh011V23nS2prWhhRBpXE5DYELbQ_BMsncOdww&oe=63E15BED&_nc_sid=8fd12b",
            )
        }

        TabLayoutMediator(binding.indicator, binding.slider) { tab, position ->
        }.attach()

        binding.btnFestival.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_festivalFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}