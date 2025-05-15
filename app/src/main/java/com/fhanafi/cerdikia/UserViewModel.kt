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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userData: Flow<UserModel> = userRepository.getUserData()

    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating
    private val _uploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> = _uploading

    fun updateGemsFromMissionReward(gemsToAdd: Int) {
        viewModelScope.launch {
            try {
                userRepository.updateGemsByQuest(gemsToAdd)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUserProfile(nama: String, email: String, kelas: Int) {
        viewModelScope.launch {
            _isUpdating.value = true
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

    fun uploadProfileImage(email: String, imageFile: File) {
        viewModelScope.launch {
            _uploading.value = true
            try {
                userRepository.uploadProfileImage(email, imageFile)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Upload failed: ${e.message}")
            } finally {
                _uploading.value = false
            }
        }
    }

    fun updatePointAndRefresh(xp: Int, gems: Int) {
        viewModelScope.launch {
            try {
                userRepository.updatePointAndRefresh(xp, gems)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshPointData() {
        viewModelScope.launch {
            try {
                userRepository.fetchPointAndSave()

                val user = userData.first()
                userRepository.getEnergy(user.email)
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun postLogSiswa(idModule: Int, idKelas: Int, idMapel: Int, skor: Int) {
        viewModelScope.launch {
            try {
                val user = userData.first() // Get latest user info (for email)
                userRepository.postLogSiswa(
                    idModule = idModule,
                    idKelas = user.kelas,
                    idMapel = idMapel,
                    email = user.email,
                    skor = skor
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
