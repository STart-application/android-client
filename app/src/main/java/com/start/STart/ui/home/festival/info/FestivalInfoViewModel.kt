package com.start.STart.ui.home.festival.info

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class FestivalInfoViewModel: ViewModel() {

    val loadFestivalInfoResult: MutableLiveData<ResultModel> = MutableLiveData()

    fun loadFestivalInfo() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.festivalService.loadBooth()
            if(res.isSuccessful) {
                loadFestivalInfoResult.postValue(ResultModel(true, data = res.body()?.data))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                loadFestivalInfoResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            loadFestivalInfoResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}