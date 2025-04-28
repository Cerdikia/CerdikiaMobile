package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.UpdateProfileRequest
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun updateUserProfile(nama: String, email: String, kelas: Int) {
        val request = UpdateProfileRequest(
            nama = nama,
            email = email,
            idKelas = kelas
        )
        val response = apiService.updateProfile(request)
        if (response.message == "Update profile berhasil") {
            // Save to DataStore
            userPreference.saveNama(nama)
            userPreference.saveEmail(email)
            userPreference.saveKelas(kelas)
        } else {
            throw Exception("Update gagal: ${response.message}")
        }
    }

    fun getUserData(): Flow<UserModel> {
        return userPreference.getUserData()
    }

    suspend fun updateUserProgress(xp: Int, gems: Int, completedId: Int) {
        userPreference.updateUserData(xp, gems, completedId)
    }

    suspend fun addCompletedMateriId(id: Int) {
        userPreference.addCompletedMateri(id)
    }

    suspend fun clearData(){
        userPreference.clearData()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(apiService, userPreference)
                INSTANCE = instance
                instance
            }
        }
    }
}
