package com.start.STart.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.ApiResponse
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.Constants
import com.start.STart.util.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class StudentInfoInputViewModel : ViewModel() {

    private val _verifyDuplicateResult: MutableLiveData<ResultModel> = MutableLiveData()
    val verifyDuplicateResult: LiveData<ResultModel> get() = _verifyDuplicateResult

    fun checkDuplicate(studentId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.checkDuplicate(studentId)

            if (res.isSuccessful) {
                _verifyDuplicateResult.postValue(ResultModel(true))
            } else {
                val body = ApiClient.parseErrorBody(res.errorBody()?.string())
                when (body.errorCode) {
                    ApiError.ST053.code -> { // 학번 중복
                        _verifyDuplicateResult.postValue(ResultModel(false, ApiError.ST053.message))
                    }
                    else -> { //
                        _verifyDuplicateResult.postValue(
                            ResultModel(
                                false,
                                "${body.errorCode}: ${body.message}"
                            )
                        )
                    }
                }
            }
        } catch (e: SocketTimeoutException) {
            _verifyDuplicateResult.postValue(
                ResultModel(false, exception = AppException.TIMEOUT)
            )
        } catch (e: ConnectException) {
            _verifyDuplicateResult.postValue(
                ResultModel(false, exception = AppException.CONNECTION)
            )
        } catch (e: Exception) {
            _verifyDuplicateResult.postValue(
                ResultModel(false, exception = AppException.UNEXPECTED)
            )
        }
    }
}