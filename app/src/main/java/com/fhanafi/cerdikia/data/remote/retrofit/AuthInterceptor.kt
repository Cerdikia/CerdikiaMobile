package com.fhanafi.cerdikia.data.remote.retrofit

import android.util.Log
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.remote.request.TokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val userPreference: UserPreference
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Skip adding Authorization header for login and register endpoints
        if (url.contains("/login") || url.contains("/register/siswa") || url.contains("/ranking?id_kelas=0")) {
            return chain.proceed(originalRequest)
        }

        val user = runBlocking { userPreference.getUserData().first() }
        val accessToken = user.accessToken
        Log.d("AuthInterceptor", "Using access token: $accessToken")
        val authenticatedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(authenticatedRequest)

        if (response.code == 401) {
            response.close()

            val newToken = runBlocking { refreshToken(userPreference) }
            Log.d("Refresh Token function get called", "New token: $newToken")
            return if (newToken != null) {
                val newRequest: Request = originalRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                chain.proceed(newRequest)
            } else {
                Log.e("AuthInterceptor", "Token refresh failed. Session expired.")
                response
            }
        }

        return response
    }

    private suspend fun refreshToken(userPreference: UserPreference): String? {
        val user = userPreference.getUserData().first()
        val refreshToken = user.refreshToken ?: return null

        return try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kp-golang-mysql2-container.raffimrg.my.id/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            val response = apiService.refreshToken(TokenRequest(refreshToken))

            if (response.isSuccessful) {
                val tokenData = response.body()?.data
                val newAccessToken = tokenData?.accessToken
                val newRefreshToken = tokenData?.refreshToken

                Log.d("AuthInterceptor", "New tokens received. Access: $newAccessToken, Refresh: $newRefreshToken")

                if (newAccessToken != null && newRefreshToken != null) {
                    userPreference.saveTokens(newAccessToken, newRefreshToken)
                    newAccessToken
                } else {
                    null
                }
            } else {
                Log.e("AuthInterceptor", "Failed to refresh token: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}