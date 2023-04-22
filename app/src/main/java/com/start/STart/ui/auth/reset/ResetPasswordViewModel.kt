package com.start.STart.ui.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.member.request.ResetPasswordWithoutLoginRequest
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordViewModel: ViewModel() {

    private val _resetPasswordResult: MutableLiveData<ResultModel> = MutableLiveData()
    val resetPasswordResult: LiveData<ResultModel> get() = _resetPasswordResult

    fun resetPassword(studentId: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.resetPasswordWithoutLogin(
                ResetPasswordWithoutLoginRequest(
                    studentNo = studentId,
                    password = password,
                )
            )

            if(res.isSuccessful) {
                _resetPasswordResult.postValue(ResultModel(true))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                when(errorBody.errorCode) {
                    ApiError.ST066.name -> { // 인증 정보 불일치
                        _resetPasswordResult.postValue(ResultModel(false, errorBody.message))
                    }
                    else -> {
                        _resetPasswordResult.postValue(ResultModel(false, errorBody.message))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _resetPasswordResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}