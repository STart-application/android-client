package com.start.STart.ui.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.start.STart.databinding.LayoutInfo1Binding
import com.start.STart.databinding.LayoutInfo2Binding
import com.start.STart.databinding.LayoutInfo3Binding

class InfoAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> Info1Fragment()
            1 -> Info2Fragment()
            else -> Info3Fragment()
        }
    }
}