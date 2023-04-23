package com.start.STart.ui.home.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.member.response.MemberData
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SettingViewModel: ViewModel() {
    private val _memberData: MutableLiveData<MemberData?> = MutableLiveData()
    val memberData: LiveData<MemberData?>
        get() = _memberData

    fun loadMemberData() = viewModelScope.launch(Dispatchers.IO) {
        val data = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
        _memberData.postValue(data)
    }

    val deleteMemberResult: MutableLiveData<ResultModel> = MutableLiveData()
    fun deleteMember() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.deleteMember()
            if(res.isSuccessful) {
                deleteMemberResult.postValue(ResultModel(true))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                deleteMemberResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: IOException) {
            deleteMemberResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }

}