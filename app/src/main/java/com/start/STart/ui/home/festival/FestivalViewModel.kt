package com.start.STart.ui.home.festival

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class FestivalViewModel: ViewModel() {

    init {
        loadStamp()
    }

    val loadStampResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun loadStamp() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.stampService.loadStamp()
            if(res.isSuccessful) {
                loadStampResult.postValue(ResultModel(true, data = res.body()?.data))
            } else {
                val errorBody = ApiClient.parseErrorBody(res.errorBody()?.string())
                loadStampResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            loadStampResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}