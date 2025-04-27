package com.fhanafi.cerdikia.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.pref.UserPreference
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AuthViewModel(private val pref: UserPreference) : ViewModel() {

    val userData: Flow<UserModel> = pref.getUserData() // SOC untuk sebelum login atau proses authentication

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
    // kemungkinan diubah
    fun saveEmail(email: String) {
        viewModelScope.launch {
            pref.saveEmail(email)
        }
    }


    // Temp holder
    private var _nama: String? = null
    private var _kelas: Int? = null
    private var _email: String? = null

    // Save locally inside ViewModel first (not DataStore)
    fun setTempNama(nama: String) {
        _nama = nama
    }

    fun setTempKelas(kelas: Int) {
        _kelas = kelas
    }

    fun setTempEmail(email: String) {
        _email = email
    }

    // After API Success, save to DataStore
    fun saveUserAfterRegister() {
        viewModelScope.launch {
            _nama?.let { pref.saveNama(it) }
            _kelas?.let { pref.saveKelas(it) }
            _email?.let { pref.saveEmail(it) }
        }
    }

    // bisa check dari API
    fun getNavigationTarget(): Flow<Class<out AppCompatActivity>> {
        return pref.getUserData().map { user ->
            if (user.nama.isEmpty() || user.kelas == 0)
                LoginActivity::class.java
            else
                MainActivity::class.java
        }
    }

    // Additional getter for current temp data if needed
    fun getTempNama() = _nama
    fun getTempKelas() = _kelas
    fun getTempEmail() = _email
}