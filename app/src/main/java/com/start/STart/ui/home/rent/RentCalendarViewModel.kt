package com.start.STart.ui.home.rent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.rent.response.RentData
import com.start.STart.model.ResultModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentCalendarViewModel: ViewModel() {

    private val _loadCalendarResult: MutableLiveData<Pair<Int, ResultModel>> = MutableLiveData()
    val loadCalendarResult: LiveData<Pair<Int, ResultModel>>
        get() = _loadCalendarResult

    fun loadCalendar(viewPagerIndex: Int, year: Int, month: Int, type: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.rentService.getCalendarData(
                year = year,
                month = month,
                category = type
            )

            if(res.isSuccessful) {
                val rentData = res.body()?.parseDataList(RentData::class.java)
                _loadCalendarResult.postValue(Pair(viewPagerIndex, ResultModel(true, data = rentData)))
            } else {
                _loadCalendarResult.postValue(Pair(viewPagerIndex, ResultModel(false)))
            }
        } catch (e: Exception) {
            _loadCalendarResult.postValue(Pair(viewPagerIndex, ResultModel(false)))

        }
    }
}