package com.start.STart.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.LoginRequest
import com.start.STart.api.auth.response.TokenData
import com.start.STart.model.LiveDataResult
import com.start.STart.util.Constants
import com.start.STart.util.PreferenceManager
import com.start.STart.util.TokenHelper
import com.start.STart.util.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginResult: MutableLiveData<LiveDataResult> = MutableLiveData()
    val loginResult: LiveData<LiveDataResult> get() = _loginResult

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
                    _loginResult.postValue(LiveDataResult(true))
                } else {
                    _loginResult.postValue(LiveDataResult(false, "로그인 실패"))
                }
            } else {
                val body = gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _loginResult.postValue(LiveDataResult(false, body.getErrorMessage()))
            }
        } catch(e: Exception) {
            e.printStackTrace()
            _loginResult.postValue(LiveDataResult(false, e.message))
        }
    }

}