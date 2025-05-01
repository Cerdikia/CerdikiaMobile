package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.LoginRequest
import com.fhanafi.cerdikia.data.remote.request.RegisterRequest
import com.fhanafi.cerdikia.data.remote.response.LoginResponse
import com.fhanafi.cerdikia.data.remote.response.RegisterResponse
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun login(email: String): LoginResponse {
        return apiService.login(LoginRequest(email = email, role = "siswa"))
    }

    suspend fun register(nama: String, email: String, kelas: Int): RegisterResponse {
        return apiService.register(RegisterRequest(nama, email, kelas))
    }

    fun getUserData(): Flow<UserModel> {
        return userPreference.getUserData()
    }

    suspend fun saveNama(nama: String) {
        userPreference.saveNama(nama)
    }

    suspend fun saveKelas(kelas: Int) {
        userPreference.saveKelas(kelas)
    }

    suspend fun saveEmail(email: String) {
        userPreference.saveEmail(email)
    }

    suspend fun savePhotoUrl(photoUrl: String) {
        userPreference.savePhotoUrl(photoUrl)
    }

    suspend fun clearData(){
        userPreference.clearData()
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        userPreference.saveTokens(accessToken, refreshToken)
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService, userPreference).also { instance = it }
            }
    }
}