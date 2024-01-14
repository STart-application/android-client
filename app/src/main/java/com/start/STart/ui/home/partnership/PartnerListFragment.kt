package com.start.STart.ui.home.partnership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.start.STart.api.partner.response.Partner
import com.start.STart.databinding.FragmentPartnerListBinding
import com.start.STart.ui.util.OnItemClickListener

class PartnerListFragment : Fragment() {

    private var _binding: FragmentPartnerListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PartnerViewModel by activityViewModels()

    val adapter = PartnerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPartnerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.partnerList.observe(viewLifecycleOwner) {
            adapter.originalList = it
        }

        viewModel.category.observe(viewLifecycleOwner) {
            val keyword = viewModel.keyword.value
            adapter.filter(it, keyword)
        }

        viewModel.keyword.observe(viewLifecycleOwner) {
            val category = viewModel.category.value?:PartnerCategory.all
            adapter.filter(category, it)
        }
    }

    private fun initRecyclerView() {
        binding.rvList.adapter = adapter
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        adapter.setOnItemClickListener(onItemClickListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PartnerListFragment()
    }
}