package com.fhanafi.cerdikia

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.data.database.AppDatabase
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.data.pref.dataStore
import com.fhanafi.cerdikia.data.remote.retrofit.ApiConfig
import com.fhanafi.cerdikia.data.repository.AuthRepository
import com.fhanafi.cerdikia.data.repository.MapelRepository
import com.fhanafi.cerdikia.data.repository.RangkingRepository
import com.fhanafi.cerdikia.data.repository.UserRepository
import com.fhanafi.cerdikia.ui.home.HomeViewModel
import com.fhanafi.cerdikia.ui.login.AuthViewModel
import com.fhanafi.cerdikia.ui.rangking.RangkingViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    private lateinit var userRepository: UserRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var rangkingRepository: RangkingRepository
    private lateinit var mapelRepository: MapelRepository

    private fun initRepositories(context: Context) {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        val database = AppDatabase.getInstance(context)
        val mapelDao = database.mapelDao()

        if (!::userRepository.isInitialized) {
            userRepository = UserRepository.getInstance(apiService, pref)
        }
        if (!::authRepository.isInitialized) {
            authRepository = AuthRepository.getInstance(apiService, pref)
        }
        if (!::rangkingRepository.isInitialized) {
            rangkingRepository = RangkingRepository.getInstance(apiService, pref)
        }
        if (!::mapelRepository.isInitialized) {
            mapelRepository = MapelRepository(apiService, mapelDao)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getInstance(context: Context): ViewModelProvider.Factory {
        initRepositories(context)
        return this
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(RangkingViewModel::class.java) -> {
                RangkingViewModel(rangkingRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mapelRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
