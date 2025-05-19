package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.mapper.toUserModel
import com.fhanafi.cerdikia.databinding.ActivityKelasBinding
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

class KelasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelasBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]
        // Baca dari Intent
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: ""
        val email = intent.getStringExtra("EXTRA_EMAIL") ?: ""
        viewModel.setTempNama(nama)
        viewModel.setTempEmail(email)
        setupListener()
    }
    // TODO: Mencheck di edit text dengan helper untuk mengecek inputan kelas agar mendapat nilai kelas yang pasti satu digit dan tidak diawali dengan 0
    private fun setupListener() {
        binding.btnCreate.isEnabled = false
        binding.etKelas.onValidInputChanged = { isValid ->
            binding.btnCreate.isEnabled = isValid
        }
        binding.btnCreate.setOnClickListener {
            val kelasStr = binding.etKelas.text.toString()
            if (kelasStr.isNotEmpty()) {
                val kelas = kelasStr.toInt()
                viewModel.setTempKelas(kelas)

                lifecycleScope.launch {
                    try {
                        val nama = viewModel.getTempNama()
                        val email = viewModel.getTempEmail()

                        if (nama == null || email == null) {
                            Toast.makeText(this@KelasActivity, "Nama atau Email kosong!", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val response = viewModel.register(nama, email, kelas)

                        if (response.message == "User dengan email $email berhasil dibuat") {
                            val loginResponse = viewModel.login(email)
                            val user: FirebaseUser? = auth.currentUser
                            user?.photoUrl?.toString()?.let { photoUrl ->
                                viewModel.savePhotoUrl(photoUrl)
                            }
                            loginResponse.data?.let { data ->
                                val userModel = data.toUserModel()
                                viewModel.saveUserData(userModel.nama, userModel.email, userModel.kelas)
                                viewModel.saveUserTokens(userModel.accessToken, userModel.refreshToken)
                                Log.d("KelasActivity", "User data saved: $userModel")
                                startActivity(Intent(this@KelasActivity, MainActivity::class.java))
                                finish()
                            } ?: run {
                                Toast.makeText(this@KelasActivity, "Login otomatis gagal setelah registrasi.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@KelasActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                binding.etKelas.error = getString(R.string.kelas_kosong)
            }
        }
    }


    private fun initialBinding(){
        binding = ActivityKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}