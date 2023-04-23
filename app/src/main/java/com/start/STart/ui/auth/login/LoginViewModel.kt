package com.start.STart.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.auth.request.LoginRequest
import com.start.STart.api.auth.response.TokenData
import com.start.STart.api.member.response.MemberData
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.Constants
import com.start.STart.util.MemberDataHelper
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult: MutableLiveData<ResultModel> = MutableLiveData()
    val loginResult: LiveData<ResultModel> get() = _loginResult

    fun login(studentId: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.login(LoginRequest(studentId, password))
            if(res.isSuccessful) {
                val tokenData = res.body()?.parseData(TokenData::class.java)
                tokenData?.let {
                    PreferenceManager.putString(Constants.KEY_REFRESH_TOKEN, it.refreshToken!!)
                    PreferenceManager.putString(Constants.KEY_ACCESS_TOKEN, it.accessToken!!)
                }

                if(TokenHelper.tryLoginWithAccessToken()) {
                    _loginResult.postValue(ResultModel(true))
                } else {
                    _loginResult.postValue(ResultModel(false, "로그인 실패"))
                }
            } else {
                val body = ApiClient.parseBody(res.errorBody()?.string())
                _loginResult.postValue(ResultModel(false, body.message))
            }
        } catch(e: Exception) {
            _loginResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
    private val _loadMemberResult: MutableLiveData<ResultModel> = MutableLiveData()
    val loadMemberResult: LiveData<ResultModel> get() = _loadMemberResult

    fun loadMember() = viewModelScope.launch(Dispatchers.IO) {
        val result = MemberDataHelper.readMember()
        if(result.isSuccessful) {
            PreferenceManager.saveToPreferences(Constants.KEY_MEMBER_DATA, result.data as MemberData)
            _loadMemberResult.postValue(result)
        }
    }

}