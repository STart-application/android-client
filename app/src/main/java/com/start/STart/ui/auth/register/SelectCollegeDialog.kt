package com.start.STart.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.marginStart
import androidx.fragment.app.DialogFragment
import com.start.STart.databinding.DialogSelectCollegeBinding
import com.start.STart.databinding.ItemDepartmentBinding
import com.start.STart.databinding.LayoutDividerBinding
import com.start.STart.util.departments
import com.start.STart.util.dp2px

class SelectCollegeDialog: DialogFragment() {
    private var _binding: DialogSelectCollegeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        departments.keys.forEach { addRadioButton(it) }
    }

    private fun addRadioButton(department: String) {
        val radioButton = ItemDepartmentBinding.inflate(layoutInflater, binding.radioGroup, false).root
        radioButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(
                context?.dp2px(4f)?.toInt() ?: 0,
                0,
                context?.dp2px(4f)?.toInt() ?: 0,
                0
            )
        }
        binding.radioGroup.addView(radioButton.apply {
            text = department
            id = View.generateViewId()
        })

        val divider = LayoutDividerBinding.inflate(layoutInflater, binding.radioGroup, false).root
        binding.radioGroup.addView(divider)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectCollegeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}