package com.start.STart.ui.home.festival

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.stamp.request.PostStampBody
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class FestivalViewModel: ViewModel() {

    val loadStampResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun loadStamp() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.stampService.loadStamp()
            if(res.isSuccessful) {
                loadStampResult.postValue(ResultModel(true, data = res.body()?.data))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                loadStampResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            loadStampResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }

    val postStampResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun postStamp(type: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.stampService.postStamp(PostStampBody(type))
            if(res.isSuccessful) {
                postStampResult.postValue(ResultModel(true))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                postStampResult.postValue(ResultModel(false, message = errorBody.message, errorCode = errorBody.errorCode))
            }
        } catch (e: IOException) {
            postStampResult.postValue(ResultModel(false, message = AppException.UNEXPECTED.title))
        }
    }
}