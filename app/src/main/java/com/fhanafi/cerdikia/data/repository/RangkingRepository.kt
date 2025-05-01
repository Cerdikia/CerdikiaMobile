package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.response.DataItem
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class RangkingRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun fetchRanking(idKelas: Int): List<DataItem> {
        val response = apiService.getRangking(idKelas)
        return response.data?.filterNotNull() ?: emptyList()
    }

    fun getUserData(): Flow<UserModel> {
        return userPreference.getUserData()
    }

    companion object {
        @Volatile
        private var INSTANCE: RangkingRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): RangkingRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = RangkingRepository(apiService, userPreference)
                INSTANCE = instance
                instance
            }
        }
    }
}
