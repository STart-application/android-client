package com.start.STart.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.banner.BannerModel
import com.start.STart.model.ResultModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel: ViewModel() {

    init {
        loadBanner()
        loadFestivalEnabled()
    }

    private val _loadBannerResult: MutableLiveData<ResultModel> = MutableLiveData()
    val loadBannerResult: LiveData<ResultModel>
        get() = _loadBannerResult

    fun loadBanner() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.bannerService.readBanner()
            if(res.isSuccessful) {
                val list = res.body()?.parseDataList(BannerModel::class.java)?.filter {
                    !it.isDeleted
                }?.sortedBy {
                    it.priority
                }
                Log.d(null, "loadBanner: ${list?.joinToString(" ")}")
                _loadBannerResult.postValue(ResultModel(true, data = list))
                return@launch
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _loadBannerResult.postValue(ResultModel(false))
        }
        _loadBannerResult.postValue(ResultModel(false))
    }

    val festivalEnabledResult: MutableLiveData<ResultModel> = MutableLiveData()
    fun loadFestivalEnabled() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.festivalService.checkFestivalPeriod()
            festivalEnabledResult.postValue(ResultModel(res.isSuccessful))
        } catch (e: IOException) {
            festivalEnabledResult.postValue(ResultModel(false))
        }
    }
}