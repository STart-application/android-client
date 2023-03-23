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

class HomeViewModel: ViewModel() {

    private val _loadBannerResult: MutableLiveData<ResultModel> = MutableLiveData()
    val loadBannerResult: LiveData<ResultModel>
        get() = _loadBannerResult

    // 배너 API 연결: 에러 처리 필요 없음
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
        }
        _loadBannerResult.postValue(ResultModel(false))
    }
}