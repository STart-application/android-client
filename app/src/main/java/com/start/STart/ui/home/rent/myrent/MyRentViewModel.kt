package com.start.STart.ui.home.rent.myrent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MyRentViewModel : ViewModel() {
    val loadMyRentResult: MutableLiveData<ResultModel> = MutableLiveData()
    fun loadMyRent() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.rentService.getMyRent()
            if(res.isSuccessful) {
                loadMyRentResult.postValue(ResultModel(true, data = res.body()?.data))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                loadMyRentResult.postValue(ResultModel(false, errorBody?.message))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            loadMyRentResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}