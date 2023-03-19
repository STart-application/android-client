package com.start.STart.ui.home.setting.suggest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuggestViewModel: ViewModel() {
    fun sendSuggestion() = viewModelScope.launch(Dispatchers.IO) {
        try {
            // TODO: 제안사항 API 연결 (디자인 완성된 이후)
        } catch (e: Exception) {

        }
    }
}