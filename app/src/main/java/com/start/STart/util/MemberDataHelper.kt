package com.start.STart.util

import com.start.STart.api.ApiClient
import com.start.STart.api.member.response.MemberData
import com.start.STart.model.ResultModel

object MemberDataHelper {
    suspend fun readMember(): ResultModel {
        try {
            val res = ApiClient.memberService.readMember()
            if(res.isSuccessful) {
                val result = res.body()?.parseData(MemberData::class.java)
                PreferenceManager.saveToPreferences(Constants.KEY_MEMBER_DATA, result)
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