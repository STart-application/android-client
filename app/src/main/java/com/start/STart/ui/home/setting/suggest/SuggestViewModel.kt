package com.start.STart.ui.home.setting.suggest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.start.STart.api.ApiClient
import com.start.STart.api.suggestion.request.SuggestRequest
import com.start.STart.model.ResultModel
import com.start.STart.util.AppException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class SuggestViewModel: ViewModel() {

    private val _suggestionResult: MutableLiveData<ResultModel> = MutableLiveData()
    val suggestionResult: LiveData<ResultModel>
        get() = _suggestionResult
    fun sendSuggestion(suggestRequest: SuggestRequest) = viewModelScope.launch(Dispatchers.IO) {
        val data = hashMapOf(
            "title" to suggestRequest.title.toRequestBody("text/plain".toMediaTypeOrNull()),
            "content" to suggestRequest.content.toRequestBody("text/plain".toMediaTypeOrNull()),
            "category" to suggestRequest.category.toRequestBody("text/plain".toMediaTypeOrNull())
        )

        try {
            val res = ApiClient.suggestionService.suggest(data, suggestRequest.file)
            if(res.isSuccessful) {
                _suggestionResult.postValue(ResultModel(true))
            } else {
                val errorBody = ApiClient.parseBody(res.errorBody()?.string())
                _suggestionResult.postValue(ResultModel(false, errorBody.message))
            }
        } catch (e: Exception) {
            _suggestionResult.postValue(ResultModel(false, AppException.UNEXPECTED.title))
        }
    }
}