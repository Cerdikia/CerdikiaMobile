package com.fhanafi.cerdikia.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.database.MapelEntity
import com.fhanafi.cerdikia.data.repository.MapelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val mapelRepository: MapelRepository) : ViewModel() {

    private val _mapels = MutableStateFlow<List<MapelEntity>>(emptyList())
    val mapels: StateFlow<List<MapelEntity>> = _mapels.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadMapels(idKelas: Int, finished: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val apiData = mapelRepository.fetchMapel(idKelas, finished)
                _mapels.value = apiData
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch data: ${e.message}"
                mapelRepository.getLocalMapels(idKelas, finished)
                    .catch { _errorMessage.value = "Failed to load local data: ${it.message}" }
                    .collect { _mapels.value = it }
            } finally {
                _isLoading.value = _mapels.value.isEmpty() // ← Keep loading true if still empty
            }
        }
    }
}