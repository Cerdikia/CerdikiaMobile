package com.fhanafi.cerdikia.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.remote.response.ListRedeemDataItem
import com.fhanafi.cerdikia.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _historyList = MutableStateFlow<List<ListRedeemDataItem>>(emptyList())
    val historyList: StateFlow<List<ListRedeemDataItem>> = _historyList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            historyRepository.getRedemptionHistory()
                .onSuccess { response ->
                    _historyList.value = response.data?.filterNotNull() ?: emptyList()
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                }
            _isLoading.value = false
        }
    }

    private val _receiptHtml = MutableStateFlow<String?>(null)
    val receiptHtml: StateFlow<String?> = _receiptHtml

    private val _receiptError = MutableStateFlow<String?>(null)
    val receiptError: StateFlow<String?> = _receiptError

    fun fetchReceiptHtml(kodePenukaran: String) {
        viewModelScope.launch {
            historyRepository.getReceiptHtml(kodePenukaran)
                .onSuccess { responseBody ->
                    _receiptHtml.value = responseBody.string()
                }
                .onFailure { error ->
                    _receiptError.value = error.message
                }
        }
    }

    fun clearReceiptHtml() {
        _receiptHtml.value = null
    }
}