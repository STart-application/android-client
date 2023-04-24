package com.start.STart.ui.auth.register.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.skydoves.cloudy.Cloudy
import com.start.STart.databinding.DialogSelectCollegeBinding
import com.start.STart.databinding.ItemDepartmentBinding
import com.start.STart.databinding.LayoutDividerBinding
import com.start.STart.ui.auth.register.StudentInfoInputViewModel
import com.start.STart.util.contains
import com.start.STart.util.departments
import com.start.STart.util.dp2px

class SelectCollegeOrDepartmentDialog: DialogFragment() {
    private var _binding: DialogSelectCollegeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(StudentInfoInputViewModel::class.java)
    }

    companion object {
        const val TYPE_COLLEGE = "type_college"
        const val TYPE_DEPARTMENT = "type_department"
    }

    var type: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.composeView.setContent {
            Cloudy(radius = 10, allowAccumulate = { true }){

            }
        }

        binding.dim.setOnTouchListener { view, motionEvent ->
            Log.d(null, "onViewCreated: ${motionEvent.rawX} ${motionEvent.rawY}")
            Log.d(null, "onViewCreated: ${binding.cardView.height}")
            if(!binding.cardView.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())) {
                dismiss()
            }
            true
        }

        binding.btnConfirm.setOnClickListener {
            when(type) {
                TYPE_COLLEGE -> {
                    val college = view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)?.run { text.toString() } ?:""
                    if(viewModel.collegeData.value != college) {
                        viewModel.updateDepartment("")
                    }
                    viewModel.updateCollege(college)
                }
                TYPE_DEPARTMENT -> {
                    viewModel.updateDepartment(view.findViewById<RadioButton>(binding.radioGroup.checkedRadioButtonId)?.run { text.toString() } ?:"")
                }

            }
            dismiss()
        }
    }

    private fun addRadioButton(department: String, checked: Boolean = false) {
        val radioButton = ItemDepartmentBinding.inflate(layoutInflater, binding.radioGroup, false).root
        radioButton.isChecked = checked
        radioButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
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

    override fun onStart() {
        super.onStart()
        binding.radioGroup.removeAllViews()
        when(type) {
            TYPE_COLLEGE -> {
                binding.textTitle.text = "단과대학 선택"
                departments.keys.forEach {
                    addRadioButton(it, it == viewModel.collegeData.value)
                }
            }
            TYPE_DEPARTMENT -> {
                binding.textTitle.text = "학과 선택"
                val department = departments[viewModel.collegeData.value]
                if (department != null) {
                    resources.getStringArray(department).forEach {
                        addRadioButton(it, it == viewModel.departmentData.value)
                    }
                }
            }
        }
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