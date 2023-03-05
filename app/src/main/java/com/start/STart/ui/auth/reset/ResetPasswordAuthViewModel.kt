package com.start.STart.ui.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.SendResetPasswordCodeRequest
import com.start.STart.api.auth.request.VerifyResetPasswordCode
import com.start.STart.model.ResultLiveDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordAuthViewModel : ViewModel() {

    private val _sendAuthCodeResult: MutableLiveData<ResultLiveDataModel> = MutableLiveData()
    val sendAuthCodeResult: LiveData<ResultLiveDataModel> get() = _sendAuthCodeResult

    fun sendAuthCodeForResetPassword(studentId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.sendResetPasswordCode(SendResetPasswordCodeRequest(studentId))
            if(res.isSuccessful) {
                _sendAuthCodeResult.postValue(ResultLiveDataModel(true))
            } else {
                val errorBody = ApiClient.gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _sendAuthCodeResult.postValue(ResultLiveDataModel(false, "${errorBody.errorCode}: ${errorBody.message}"))
                /*
                    ST041: 찾을 수 없는 회원
                    ST057: 학생증 인증 중인 회원
                    ST058: 탈퇴한 회원
                    ST065: 인증 횟수 초과
                */
            }
        } catch (e: Exception) {
            // TODO: 에러 처리
        }
    }

    private val _verifyAuthCodeResult: MutableLiveData<ResultLiveDataModel> = MutableLiveData()
    val verifyAuthCodeResult: LiveData<ResultLiveDataModel> get() = _verifyAuthCodeResult

    fun verifyAuthCode(studentId: String, authCode: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.verifyResetPasswordCode(VerifyResetPasswordCode(studentId, authCode))
            if(res.isSuccessful) {
                _verifyAuthCodeResult.postValue(ResultLiveDataModel(true))
            } else {
                val errorBody = ApiClient.gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _verifyAuthCodeResult.postValue(ResultLiveDataModel(false, "${errorBody.errorCode}: ${errorBody.message}"))
                /*
                    ST066: 인증 정보 불일치
                    ST077: 기간 만료
                */
            }

        } catch(e: Exception) {
            // TODO: 에러 처리
        }
    }
}