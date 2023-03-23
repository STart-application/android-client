package com.start.STart.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiResponse
import com.start.STart.api.member.response.MemberData
import com.start.STart.model.ResultModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MemberDataHelper {
    suspend fun readMember(): ResultModel {
        try {
            val res = ApiClient.memberService.readMember()
            if(res.isSuccessful) {
                val result = res.body()?.parseData(MemberData::class.java)

                return ResultModel(true, data = result)
            } else {
                return ResultModel(false, res.errorBody()?.string())
            }
        } catch(e: Exception) {
            e.printStackTrace()
            return ResultModel(false, e.message)
        }
    }
}