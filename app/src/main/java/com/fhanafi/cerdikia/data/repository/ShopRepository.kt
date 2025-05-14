package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.MisiHarianData
import com.fhanafi.cerdikia.data.pref.MisiHarianPreference
import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShopRepository private constructor(
    private val apiService: ApiService,
    private val misiHarianPreference: MisiHarianPreference
){

    // Expose default quest data for initial StateFlow
    fun getDefaultQuestData(): MisiHarianData = MisiHarianData.getDefaultInstance()

    // Gifts: Local in-memory cache using StateFlow
    private val _gifts = MutableStateFlow<List<HadiahDataItem>>(emptyList())
    val gifts = _gifts.asStateFlow()

    suspend fun loadGifts() {
        try {
            val response = apiService.getGifts()
            _gifts.value = response.data?.filterNotNull() ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            // You can add logging or error handling here
        }
    }

    // LOCAL: expose quest flow
    val questFlow: Flow<MisiHarianData> = misiHarianPreference.questFlow

    // LOCAL: functions to update quest state
    suspend fun addXpEarned(amount: Int) = misiHarianPreference.addXpEarned(amount)

    suspend fun incrementQuizCompleted() = misiHarianPreference.incrementQuizCompleted()

    suspend fun addStudyMinutes(minutes: Int) = misiHarianPreference.addStudyMinutes(minutes)

    suspend fun checkAndResetIfNeeded() = misiHarianPreference.checkAndResetIfNeeded()

    suspend fun setXpRewardClaimed(claimed: Boolean) =
        misiHarianPreference.setXpRewardClaimed(claimed)

    suspend fun setQuizRewardClaimed(claimed: Boolean) =
        misiHarianPreference.setQuizRewardClaimed(claimed)

    suspend fun setStudyTimeRewardClaimed(claimed: Boolean) =
        misiHarianPreference.setStudyTimeRewardClaimed(claimed)

    companion object {
        @Volatile
        private var instance: ShopRepository? = null

        fun getInstance(apiService: ApiService, misiHarianPreference: MisiHarianPreference): ShopRepository {
            return instance ?: synchronized(this) {
                instance ?: ShopRepository(apiService, misiHarianPreference)
            }
        }
    }
}