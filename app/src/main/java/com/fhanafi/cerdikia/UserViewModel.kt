package com.fhanafi.cerdikia

import android.util.Log
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
            _isUpdating.value = false
            try {
                // 1. Update dari server
                val updatedUser = userRepository.updateProfileFromApi(nama, email, kelas)

                // 2. Save ke DataStore
                userRepository.saveUserDataLocally(updatedUser)
                Log.d("UserViewModel", "User data updated: $updatedUser")
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
