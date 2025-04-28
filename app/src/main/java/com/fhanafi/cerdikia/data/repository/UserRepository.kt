package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.UpdateProfileRequest
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun updateProfileFromApi(nama: String, email: String, kelas: Int): UserModel {
        val request = UpdateProfileRequest(
            nama = nama,
            email = email,
            idKelas = kelas
        )
        val response = apiService.updateProfile(request)

        if (response.message.equals("Success", ignoreCase = true)) { // <<-- Ganti disini
            val currentUser = getUserData().first() // Ambil data lama dari DataStore

            return UserModel(
                nama = response.data?.nama ?: currentUser.nama, // kalau null, pakai data lama
                email = response.data?.email ?: currentUser.email,
                kelas = response.data?.idKelas ?: currentUser.kelas,
                xp = currentUser.xp,
                gems = currentUser.gems,
                completedMateriIds = currentUser.completedMateriIds
            )
        } else {
            throw Exception("Update gagal: ${response.message}")
        }
    }

    suspend fun saveUserDataLocally(user: UserModel) {
        userPreference.saveUser(user)
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
