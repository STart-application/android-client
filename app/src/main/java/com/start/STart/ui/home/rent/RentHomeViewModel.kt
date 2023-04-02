package com.start.STart.ui.home.rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.member.response.MemberData
import com.start.STart.model.ResultModel
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentHomeViewModel: ViewModel() {
    private val _memberData: MutableLiveData<ResultModel> = MutableLiveData()
    val memberData: LiveData<ResultModel>
        get() = _memberData

    fun loadMemberData() = viewModelScope.launch(Dispatchers.IO) {
        val data = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
        if (data != null) {
            _memberData.postValue(ResultModel(true, data = data))
        } else {
            _memberData.postValue(ResultModel(false))

        }
    }
}