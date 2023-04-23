package com.start.STart.ui.home.rent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.rent.request.PostRentBody
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class RentViewModel : ViewModel() {

    val postRentResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun postRent(postRentBody: PostRentBody) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.rentService.postRent(postRentBody)
            if(res.isSuccessful) {
                val body = res.body()
                postRentResult.postValue(ResultModel(true, body?.message))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                postRentResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: IOException) {
            postRentResult.postValue(ResultModel(false,AppException.UNEXPECTED.title))
        }
    }
}