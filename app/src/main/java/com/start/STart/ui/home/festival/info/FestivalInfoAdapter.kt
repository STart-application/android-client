package com.start.STart.ui.home.festival.info

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.start.STart.ui.home.festival.info.contents.ContentsFragment

class FestivalInfoAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        if(position == 0) {
            return ContentsFragment()
        } else {
            return LineUpFragment()
        }
    }
}