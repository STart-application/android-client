package com.start.STart.ui.home.rent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.rent.response.RentData
import com.start.STart.api.rent.response.RentItemCount
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RentCalendarViewModel: ViewModel() {

    private val _loadCalendarResult: MutableLiveData<Pair<Int, ResultModel>> = MutableLiveData()
    val loadCalendarResult: LiveData<Pair<Int, ResultModel>>
        get() = _loadCalendarResult


    fun loadCalendar(viewPagerIndex: Int, year: Int, month: Int, category: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val rentDateList = async { loadRentData(year, month, category) }
            val itemTotalCount = async { loadItemCount(category) }

            if (rentDateList.await() != null && itemTotalCount.await() != null) {

                val group = rentDateList.await()?.groupBy {
                    it.startTime
                }

                _loadCalendarResult.postValue(Pair(viewPagerIndex, ResultModel(true, data = group)))

            } else {
                _loadCalendarResult.postValue(Pair(viewPagerIndex, ResultModel(false)))
            }
        }


    private suspend fun loadRentData(year: Int, month: Int, category: String): List<RentData> ?{
        try {
            val res = ApiClient.rentService.getCalendarData(
                year = year,
                month = month,
                category = category
            )

            if(res.isSuccessful) {
                val rentDataList = res.body()?.parseDataList(RentData::class.java)
                return rentDataList
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    val loadItemCountResult: MutableLiveData<ResultModel> = MutableLiveData()

    private suspend fun loadItemCount(category: String) : Int? {
        try {
            val res = ApiClient.rentService.getItemCount(category)
            if(res.isSuccessful) {
                val rentItemCount = res.body()?.parseData(RentItemCount::class.java)
                loadItemCountResult.postValue(ResultModel(true, data = rentItemCount?.count))
                return rentItemCount?.count
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                loadItemCountResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loadItemCountResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
        return 0
    }
}