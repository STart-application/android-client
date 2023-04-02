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
import com.start.STart.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

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
                val errorBody = ApiClient.parseErrorBody(res.errorBody()?.string())
                when(errorBody.errorCode) {
                    ApiError.ST066.code -> { // 인증 정보 불일치
                        _resetPasswordResult.postValue(ResultModel(false, errorBody.message))
                    }
                    else -> {
                        _resetPasswordResult.postValue(ResultModel(false, errorBody.message))
                    }
                }
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            _resetPasswordResult.postValue(ResultModel(false, AppException.TIMEOUT.title))
        } catch (e: ConnectException) {
            e.printStackTrace()
            _resetPasswordResult.postValue(ResultModel(false, AppException.CONNECTION.title))
        } catch (e: Exception) {
            e.printStackTrace()
            _resetPasswordResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}