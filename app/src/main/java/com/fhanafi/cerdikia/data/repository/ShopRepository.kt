package com.fhanafi.cerdikia.data.repository

import com.fhanafi.cerdikia.data.pref.MisiHarianData
import com.fhanafi.cerdikia.data.pref.MisiHarianPreference
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.EmailVerifRequest
import com.fhanafi.cerdikia.data.remote.request.NotificationsItem
import com.fhanafi.cerdikia.data.remote.response.EmailVerifResponse
import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem
import com.fhanafi.cerdikia.data.remote.response.VerifData
import com.fhanafi.cerdikia.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ShopRepository private constructor(
    private val apiService: ApiService,
    private val misiHarianPreference: MisiHarianPreference,
    private val userPreference: UserPreference
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

    // Get Verifed status from API
    suspend fun getVerifiedStatus(): VerifData? {
        return try {
            val email = userPreference.getUserData().first().email
            val response = apiService.getVerified(email)
            response.data // can be null if not verified
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun sendEmailVerification(): Result<EmailVerifResponse> {
        return try {
            val email = userPreference.getUserData().first().email
            val request = EmailVerifRequest(
                notifications = listOf(
                    NotificationsItem(
                        form = email,
                        entity = "role",
                        dest = "admin",
                        subject = "Meminta Verifikasi Akun Cerdikia",
                        message = """
                            Halo Admin, 
                            Saya ingin meminta verifikasi untuk akun saya dengan email $email.
                            Mohon segera melakukan proses verifikasi akun ini melalui dashboard administrasi.
                            Terima Kasih.
                            Salam Hangat dan Semangat Belajar,
                        """.trimIndent(),
                        status = "mengirim"
                    )
                )
            )
            val response = apiService.sendEmailVerif(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
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

        fun getInstance(apiService: ApiService, misiHarianPreference: MisiHarianPreference, userPreference: UserPreference): ShopRepository {
            return instance ?: synchronized(this) {
                instance ?: ShopRepository(apiService, misiHarianPreference, userPreference)
            }
        }
    }
}