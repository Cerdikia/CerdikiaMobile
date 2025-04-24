package com.fhanafi.cerdikia.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.data.pref.UserPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AuthViewModel(private val pref: UserPreference) : ViewModel() {

    fun saveNama(nama: String, onDone: () -> Unit){
        viewModelScope.launch {
            pref.saveNama(nama)
            onDone()
        }
    }

    fun saveKelas(kelas: Int){
        viewModelScope.launch {
            pref.saveKelas(kelas)
        }
    }

    fun getNavigationTarget(): Flow<Class<out AppCompatActivity>> {
        return pref.getUserData().map { user ->
            if (user.nama.isEmpty() || user.kelas == 0)
                LoginActivity::class.java
            else
                MainActivity::class.java
        }
    }

}