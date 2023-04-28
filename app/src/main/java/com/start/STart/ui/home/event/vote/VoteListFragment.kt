package com.start.STart.ui.home.event.vote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.start.STart.databinding.FragmentVoteListBinding
import com.start.STart.ui.home.event.vote.VoteAdapter

class VoteListFragment : Fragment() {

    lateinit var binding: FragmentVoteListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.voteList.adapter = VoteAdapter()
    }

}