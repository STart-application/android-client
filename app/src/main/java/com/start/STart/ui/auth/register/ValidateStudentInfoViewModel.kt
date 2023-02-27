package com.start.STart.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class ValidateStudentInfoViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Boolean> = MutableLiveData()
    val isDuplicate: LiveData<Boolean> get() = _isDuplicate

    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun checkDuplicate(studentId: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = ApiClient.memberService.checkDuplicate(studentId)
            if (res.isSuccessful) {
                _isDuplicate.postValue(false)
            } else {
                if(res.code() == 404) {
                    _errorMessage.postValue("404")
                } else {
                    val body = ApiClient.gson.fromJson(res.errorBody()?.string(), ApiResponse::class.java)
                    when(body.errorCode) {
                        ApiError.ST053.code -> _isDuplicate.postValue(true)
                        else -> {
                            _errorMessage.postValue("${body.errorCode} ${body.message}")
                        }
                    }
                }
            }
        } catch (e: IOException) {
            _errorMessage.postValue(e.message)
        } catch (e: JsonSyntaxException) {
            _errorMessage.postValue(e.message)
        }
    }
}