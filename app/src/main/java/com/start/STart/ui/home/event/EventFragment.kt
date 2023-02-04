package com.start.STart.ui.home.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.start.STart.R
import com.start.STart.databinding.FragmentEventBinding

class EventFragment : Fragment() {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private val eventAdapter by lazy { EventAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvEvent.adapter = eventAdapter.apply {
            list = listOf(
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
                Event("이벤트 항목", "D-1"),
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}