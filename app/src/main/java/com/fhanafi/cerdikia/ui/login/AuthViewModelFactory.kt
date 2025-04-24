package com.fhanafi.cerdikia.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.data.pref.UserPreference

class AuthViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val pref = UserPreference.getInstance(context)
        return AuthViewModel(pref) as T
    }
}