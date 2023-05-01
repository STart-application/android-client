package com.start.STart.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.member.request.RegisterData
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import com.start.STart.util.Constants
import com.start.STart.util.toPlainRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.IOException
import java.net.SocketTimeoutException

class StudentCardUploadViewModel: ViewModel() {

    private val _registerResult: MutableLiveData<ResultModel> = MutableLiveData()
    val registerResult: LiveData<ResultModel> get() = _registerResult

    var timeOutCnt = 0

    fun register(registerData: RegisterData, filePart: MultipartBody.Part?) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.register(
                hashMapOf(
                    Constants.KEY_API_STUDENT_NO to (registerData.studentNo).toPlainRequestBody(),
                    Constants.KEY_API_NAME to (registerData.name).toPlainRequestBody(),
                    Constants.KEY_API_DEPARTMENT to (registerData.department).toPlainRequestBody(),
                    Constants.KEY_API_PASSWORD to (registerData.appPassword).toPlainRequestBody(),
                    Constants.KEY_API_PHONE_NO to (registerData.phoneNo).toPlainRequestBody(),
                    Constants.KEY_API_FCM_TOKEN to (registerData.fcmToken).toPlainRequestBody(),
                ),
                filePart,
            )
            if(res.isSuccessful) {
                _registerResult.postValue(ResultModel(true))
            } else {
                val body = ApiClient.parseBody(res.errorBody()?.string())
                if(timeOutCnt == 1 && body?.errorCode == ApiError.ST053.name) {
                    _registerResult.postValue(ResultModel(true))
                } else {
                    _registerResult.postValue(ResultModel(false, body?.message))
                }
            }
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            timeOutCnt += 1
            _registerResult.postValue(ResultModel(false, exception = AppException.TIMEOUT))

        } catch(e: IOException) {
            e.printStackTrace()
            _registerResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}