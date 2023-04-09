package com.start.STart.ui.auth.register

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.marginStart
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.start.STart.api.member.request.RegisterData
import com.start.STart.databinding.DialogSelectCollegeBinding
import com.start.STart.databinding.ItemDepartmentBinding
import com.start.STart.databinding.LayoutDividerBinding
import com.start.STart.util.contains
import com.start.STart.util.departments
import com.start.STart.util.dp2px

class SelectCollegeDialog: DialogFragment() {
    private var _binding: DialogSelectCollegeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(StudentInfoInputViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardViewRect = Rect()

        departments.keys.forEach { addRadioButton(it) }
        binding.cardView.requestLayout()


        binding.root.setOnClickListener {
            if(cardViewRect.contains(it.x.toInt(), it.y.toInt())){
                return@setOnClickListener
            }

            dismiss()
        }


        binding.btnConfirm.setOnClickListener {
            viewModel.updateData((viewModel.registerLiveData.value?: RegisterData()).apply {
                view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)?.run {
                    college = text.toString()
                }
            })
            dismiss()
        }
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