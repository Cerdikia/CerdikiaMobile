package com.fhanafi.cerdikia.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.data.pref.UserPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AuthViewModel(private val pref: UserPreference) : ViewModel() {

    fun saveNama(nama: String){
        viewModelScope.launch {
            pref.saveNama(nama)
        }
    }

    fun saveKelas(kelas: Int){
        viewModelScope.launch {
            pref.saveKelas(kelas)
        }
    }

    fun isUserRegistered(): Flow<Boolean> {
        return pref.getUserData().map { user ->
            user.nama.isNotBlank() && user.kelas > 0
        }
    }
}