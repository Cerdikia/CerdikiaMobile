package com.fhanafi.cerdikia.data.remote.retrofit

import com.fhanafi.cerdikia.data.pref.UserPreference
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Header

class AuthInterceptor(
    private val userPreference: UserPreference
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val user = runBlocking { userPreference.getUserData().first() }
        val accessToken = user.accessToken

        // Inject token
        val authenticatedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        // Kalau unauthorized 401
        if (response.code == 401) {
            response.close() // tutup response lama

            // Try refresh token
            val newToken = runBlocking { refreshToken(userPreference) }

            return if (newToken != null) {
                // Retry request dengan token baru
                val newRequest: Request = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                chain.proceed(newRequest)
            } else {
                // Refresh token gagal, kirim response asli
                response
            }
        }

        return response
    }

    private suspend fun refreshToken(userPreference: UserPreference): String? {
        val user = userPreference.getUserData().first()
        val refreshToken = user.refreshToken

        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kp-golang-mysql2-container.raffimrg.my.id/") // Ganti sama BASE_URL kamu
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val refreshService = retrofit.create(RefreshService::class.java)

            val response = refreshService.refreshToken("Bearer $refreshToken")
            if (response.isSuccessful) {
                val newAccessToken = response.body()?.accessToken
                val newRefreshToken = response.body()?.refreshToken

                if (newAccessToken != null && newRefreshToken != null) {
                    // Simpan token baru ke DataStore
                    userPreference.saveTokens(newAccessToken, newRefreshToken)
                    newAccessToken
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// API Service untuk refresh
interface RefreshService {
    @POST("/refresh") // Ganti path ke endpoint refresh token kamu
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): retrofit2.Response<RefreshTokenResponse>
}

// Response Model
data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)