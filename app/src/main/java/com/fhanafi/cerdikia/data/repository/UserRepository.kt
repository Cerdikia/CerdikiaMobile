package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.LogsSiswaRequest
import com.fhanafi.cerdikia.data.remote.request.UpdatePointRequest
import com.fhanafi.cerdikia.data.remote.request.UpdateProfileRequest
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun updatePointAndRefresh(xp: Int, gems: Int) {
        val currentUser = userPreference.getUserData().first()
        val currentXp = currentUser.xp
        val currentGems = currentUser.gems

        val updatedXp = currentXp + xp
        val updatedGems = currentGems + gems

        val request = UpdatePointRequest(
            exp = updatedXp,
            diamond = updatedGems,
            email = currentUser.email
        )

        val response = apiService.updatePoint(request)

        val updated = response.data
        if (updated != null) {
            // Save the updated XP and Gems to DataStore
            userPreference.updateGemsAndXP(
                gems = updated.diamond ?: updatedGems,
                xp = updated.exp ?: updatedXp
            )
        } else {
            throw Exception("UpdatePointResponse ${response.message}")
        }
    }

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

    suspend fun fetchPointAndSave() {
        val response = apiService.getPoint()
        if (response.message.equals("Data retrieved successfully", ignoreCase = true)) {
            val currentUser = getUserData().first()

            val newUser = UserModel(
                nama = currentUser.nama,
                email = currentUser.email,
                kelas = currentUser.kelas,
                xp = response.data?.exp ?: currentUser.xp, // Update dari API
                gems = response.data?.diamond ?: currentUser.gems, // Update dari API
                completedMateriIds = currentUser.completedMateriIds
            )

            userPreference.saveUser(newUser)
        } else {
            throw Exception("Fetch point failed: ${response.message}")
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

    suspend fun postLogSiswa(idModule: Int, idKelas: Int, idMapel: Int, email: String, skor: Int) {
        val request = LogsSiswaRequest(idModule = idModule, idKelas = idKelas, idMapel = idMapel, email = email, skor = skor)

        val response = apiService.postLogSiswa(request)

        if (!response.message!!.contains("Success", ignoreCase = true)) {
            throw Exception("Failed to post log: ${response.message}")
        }
    }

    suspend fun getEnergy(email: String) {
        val response = apiService.getEnergy(email)

        if (!response.message.orEmpty().contains("successful", ignoreCase = true)) {
            throw Exception("Failed to get Energy: ${response.message}")
        }

        val energyValue = response.data?.energy
        if (energyValue != null) {
            val currentUser = userPreference.getUserData().first()

            val updatedUser = currentUser.copy(
                energy = energyValue // only updating energy
            )

            userPreference.saveUser(updatedUser)
        } else {
            throw Exception("Energy data is null")
        }
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
