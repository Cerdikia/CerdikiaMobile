package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class HistoryRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
){

    suspend fun getRedemptionHistory() = runCatching {
        val email = pref.getUserData().first().email
        apiService.getListRedemption(email)
    }

    suspend fun getReceiptHtml(kodePenukaran: String) = runCatching {
        apiService.getWebReceipt(kodePenukaran)
    }

    companion object{
        @Volatile
        private var instace: HistoryRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): HistoryRepository{
            return instace ?: synchronized(this){
                instace ?: HistoryRepository(apiService, userPreference)
            }
        }
    }
}