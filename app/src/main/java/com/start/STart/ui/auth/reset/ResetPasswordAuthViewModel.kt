package com.start.STart.ui.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.SendResetPasswordCodeRequest
import com.start.STart.api.auth.request.VerifyResetPasswordCode
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.TIMER_ENDED
import com.start.STart.util.gson
import com.start.STart.util.timerFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResetPasswordAuthViewModel : ViewModel() {

    private val _sendAuthCodeResult: MutableLiveData<ResultModel> = MutableLiveData()
    val sendAuthCodeResult: LiveData<ResultModel> get() = _sendAuthCodeResult

    fun sendAuthCodeForResetPassword(studentId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.sendResetPasswordCode(SendResetPasswordCodeRequest(studentId))
            if(res.isSuccessful) {
                _sendAuthCodeResult.postValue(ResultModel(true))
            } else {
                val errorBody = gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _sendAuthCodeResult.postValue(ResultModel(false, errorBody.message))
                /*
                    ST041: 찾을 수 없는 회원
                    ST057: 학생증 인증 중인 회원
                    ST058: 탈퇴한 회원
                    ST065: 인증 횟수 초과
                */
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _sendAuthCodeResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }

    // 타이머 관련 코드
    val timerLiveData = MutableLiveData<Long>()
    private var timerTask: Job? = null

    fun restartTimer() {
        stopTimer()
        startTimer()
    }

    fun startTimer() {
        if(timerTask == null) {
            timerTask = viewModelScope.launch(Dispatchers.IO) {
                timerFlow().collect {
                    timerLiveData.postValue(it)
                }
                timerLiveData.postValue(TIMER_ENDED)
            }
        }
    }

    fun stopTimer() {
        timerTask?.cancel()
        timerTask = null
    }

    private val _verifyAuthCodeResult: MutableLiveData<ResultModel> = MutableLiveData()
    val verifyAuthCodeResult: LiveData<ResultModel> get() = _verifyAuthCodeResult

    fun verifyAuthCode(studentId: String, authCode: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.verifyResetPasswordCode(VerifyResetPasswordCode(studentId, authCode))
            if(res.isSuccessful) {
                _verifyAuthCodeResult.postValue(ResultModel(true))
            } else {
                val errorBody = gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _verifyAuthCodeResult.postValue(ResultModel(false, errorBody.message))
                /*
                    ST066: 인증 정보 불일치
                    ST077: 기간 만료
                */
            }

        } catch(e: Exception) {
            e.printStackTrace()
            _verifyAuthCodeResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}