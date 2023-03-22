package com.start.STart.ui.home.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.member.response.MemberData
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel: ViewModel() {
    private val _memberData: MutableLiveData<MemberData?> = MutableLiveData()
    val memberData: LiveData<MemberData?>
        get() = _memberData

    fun loadMemberData() = viewModelScope.launch {
        val data = PreferenceManager.loadFromPreferences<MemberData>(Constants.KEY_MEMBER_DATA)
        _memberData.postValue(data)
    }
}