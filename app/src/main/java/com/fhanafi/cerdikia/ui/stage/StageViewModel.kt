package com.fhanafi.cerdikia.ui.stage

import androidx.lifecycle.*
import com.fhanafi.cerdikia.data.remote.response.EnergyData
import com.fhanafi.cerdikia.data.repository.MateriRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StageViewModel(private val materiRepository: MateriRepository) : ViewModel() {

    private val _materiList = MutableStateFlow<List<MateriItem>>(emptyList())
    val materiList: StateFlow<List<MateriItem>> = _materiList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadMateri(idMapel: Int, idKelas: Int, email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val materi = materiRepository.getMateriList(idMapel, idKelas, email)
                _materiList.value = materi
            } catch (e: Exception) {
                _materiList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun useEnergy(email: String) {
        viewModelScope.launch {
            try {
                materiRepository.useEnergy(email)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getEnergy(email: String): EnergyData? {
        return try {
            materiRepository.getEnergy(email)
        } catch (e: Exception) {
            null
        }
    }
}