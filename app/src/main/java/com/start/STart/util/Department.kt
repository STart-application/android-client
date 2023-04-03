package com.start.STart.util

import android.content.Context
import com.start.STart.R

val departments = hashMapOf(
    "공과대학" to R.array.department_1,
    "정보통신대학" to R.array.department_2,
    "에너지바이오대학" to R.array.department_3,
    "조형대학" to R.array.department_4,
    "인문사회대학" to R.array.department_5,
    "기술경영융합대학" to R.array.department_6,
    "미래융합대학" to R.array.department_7,
    "창의융합대학" to R.array.department_8,
    "교양대학" to R.array.department_9,
)

fun Context.getCollegeByDepartment(department: String): String? {

    for ((college, _) in departments) {
        val departmentArray = departments[college]?.let { arrayId ->
            this.resources.getStringArray(arrayId)
        }
        if (departmentArray != null && departmentArray.contains(department)) {
            return college
        }
    }
    return null
}
