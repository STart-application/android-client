package com.start.STart.ui.home.partnership

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.partner.response.Partner
import com.start.STart.api.partner.response.PartnerList
import com.start.STart.util.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import java.lang.Exception

class PartnerViewModel: ViewModel() {
    private val _category = MutableLiveData<PartnerCategory>()
    val category: LiveData<PartnerCategory> get() = _category

    fun setCategory(category: PartnerCategory) {
        _category.value = category
    }

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String> get() = _keyword

    fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    private val _menuVisibility = MutableLiveData<Boolean>(false)
    val menuVisibility: LiveData<Boolean> get() = _menuVisibility

    fun toggleMenu() {
        _menuVisibility.value = !(_menuVisibility.value)!!
    }

    fun showMenu() {
        _menuVisibility.value = true
    }

    fun closeMenu() {
        _menuVisibility.value = false
    }

    private val _partnerList = MutableLiveData<List<Partner>>()
    val partnerList: LiveData<List<Partner>> get() = _partnerList

    fun loadPartner() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.partnerService.getList(count = 999)
            if(res.isSuccessful) {
                val data = res.body()?.data

                val list = data?.map {
                    gson.fromJson(gson.toJson(it), PartnerList::class.java)
                } as List<PartnerList>

                val realData = list[0].partnerList

                _partnerList.postValue(realData)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}