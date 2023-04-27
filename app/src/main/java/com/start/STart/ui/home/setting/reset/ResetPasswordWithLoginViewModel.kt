package com.start.STart.ui.home.setting.reset

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.member.request.ResetPasswordWithLoginBody
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ResetPasswordWithLoginViewModel: ViewModel() {

    val resetPasswordResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun resetPassword(body: ResetPasswordWithLoginBody) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.resetPasswordWithLogin(body)
            if(res.isSuccessful) {
                resetPasswordResult.postValue(ResultModel(true))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                resetPasswordResult.postValue(ResultModel(false, errorBody?.message))
            }
        } catch (e: IOException) {
            resetPasswordResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }

}