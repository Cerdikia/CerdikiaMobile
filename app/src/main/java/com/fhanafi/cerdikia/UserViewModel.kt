package com.fhanafi.cerdikia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userData: Flow<UserModel> = userRepository.getUserData()

    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating

    fun updateUserProfile(nama: String, email: String, kelas: Int) {
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                userRepository.updateUserProfile(nama, email, kelas)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isUpdating.value = false
            }
        }
    }

    fun updateUserProgress(xp: Int, gems: Int, completedId: Int) {
        viewModelScope.launch {
            userRepository.updateUserProgress(xp, gems, completedId)
        }
    }

    fun addCompletedMateriId(id: Int) {
        viewModelScope.launch {
            userRepository.addCompletedMateriId(id)
        }
    }

    fun clearData() {
        viewModelScope.launch {
            userRepository.clearData()
        }
    }
}
