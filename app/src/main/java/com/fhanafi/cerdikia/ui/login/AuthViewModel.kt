package com.fhanafi.cerdikia.ui.login

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.data.pref.UserModel
import com.fhanafi.cerdikia.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    val userData: Flow<UserModel> = repository.getUserData()

    fun saveNama(nama: String) { viewModelScope.launch { repository.saveNama(nama) } }
    fun saveKelas(kelas: Int) { viewModelScope.launch { repository.saveKelas(kelas) } }
    fun saveEmail(email: String) { viewModelScope.launch { repository.saveEmail(email) } }

    private var _nama: String? = null
    private var _kelas: Int? = null
    private var _email: String? = null

    fun setTempNama(nama: String) { _nama = nama }
    fun setTempKelas(kelas: Int) { _kelas = kelas }
    fun setTempEmail(email: String) { _email = email }

    fun getTempNama() = _nama
    fun getTempKelas() = _kelas
    fun getTempEmail() = _email

    suspend fun login(email: String) = repository.login(email)
    suspend fun register(nama: String, email: String, kelas: Int) = repository.register(nama, email, kelas)

    fun saveUserAfterRegister() {
        viewModelScope.launch {
            _nama?.let { repository.saveNama(it) }
            _kelas?.let { repository.saveKelas(it) }
            _email?.let { repository.saveEmail(it) }
        }
    }

    suspend fun saveUserData(nama: String, email: String, kelas: Int) {
        repository.saveNama(nama)
        repository.saveEmail(email)
        repository.saveKelas(kelas)
    }

    suspend fun getNavigationTarget(): Class<out AppCompatActivity> {
        val user = repository.getUserData().first() // ini baru bener, .first() ambil data sekali
        val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
        Log.d("AuthViewModel", "isLoggedIn: $isLoggedIn, user: $user")
        return if (isLoggedIn && user.nama.isNotEmpty() && user.kelas != 0) {
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }
    }
}