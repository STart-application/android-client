package com.start.STart.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.ApiResponse
import com.start.STart.api.auth.request.SendSmsCodeRequest
import com.start.STart.api.auth.request.VerifySmsCodeRequest
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class VerifyCodeViewModel: ViewModel() {

    private val _sendCodeResult: MutableLiveData<ResultModel> = MutableLiveData()
    val sendCodeResult: LiveData<ResultModel> get() = _sendCodeResult

    private val _verifyCodeResult: MutableLiveData<ResultModel> = MutableLiveData()
    val verifyCodeResult: LiveData<ResultModel> get() = _verifyCodeResult

    // SMS 전송
    fun sendCode(phone: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.sendSmsCode(SendSmsCodeRequest(phone))
            if(res.code() == 200) {
                // 전송 성공
                _sendCodeResult.postValue(ResultModel(true))
            } else {
                val body = ApiClient.parseErrorBody(res.errorBody()?.string())
                when(body.errorCode) {
                    ApiError.ST064.name -> _sendCodeResult.postValue(ResultModel(false, ApiError.ST064.message))
                    else -> {
                        // TODO: 예외 처리
                        _sendCodeResult.postValue(ResultModel(false, body.message))
                    }
                }
            }
        } catch (e: JsonSyntaxException) {
            _sendCodeResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        } catch (e: IOException) {
            _sendCodeResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }

    // SMS 코드 검증
    fun verifyCode(phone: String, code: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.authService.verifySmsCode(VerifySmsCodeRequest(phone, code))

            if(res.code() == 200) {
                _verifyCodeResult.postValue(ResultModel(true))
            } else {
                val errorBody = gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                _verifyCodeResult.postValue(ResultModel(false, ApiError.getErrorMessage(errorBody.errorCode!!)))
            }
        } catch(e: Exception) {
            _verifyCodeResult.postValue(ResultModel(false, e.message))
        }
    }
}