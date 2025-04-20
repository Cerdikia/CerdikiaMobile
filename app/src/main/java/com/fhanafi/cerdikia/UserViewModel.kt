package com.fhanafi.cerdikia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val userPreference: UserPreference) : ViewModel() {

    val userData: Flow<UserModel> = userPreference.getUserData()

    fun updateUserProgress(xp: Int, gems: Int, completedId: Int) {
        viewModelScope.launch {
            userPreference.updateUserData(xp, gems, completedId)
        }
    }

    fun addCompletedMateriId(id: Int) {
        viewModelScope.launch {
            userPreference.addCompletedMateri(id)
        }
    }
}

class UserViewModelFactory(private val pref: UserPreference) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
