package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.data.remote.request.RegisterRequest
import com.fhanafi.cerdikia.data.remote.retrofit.ApiConfig
import com.fhanafi.cerdikia.databinding.ActivityKelasBinding
import kotlinx.coroutines.launch

class KelasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelasBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]
        // Baca dari Intent
        val nama = intent.getStringExtra("EXTRA_NAMA") ?: ""
        val email = intent.getStringExtra("EXTRA_EMAIL") ?: ""

        viewModel.setTempNama(nama)
        viewModel.setTempEmail(email)
        setupListener()
    }
    // TODO: Mencheck di edit text dengan helper untuk mengecek inputan kelas agar mendapat nilai kelas yang pasti satu digit dan tidak diawali dengan 0
    private fun setupListener() {
        binding.btnCreate.setOnClickListener {
            val kelasStr = binding.etKelas.text.toString()
            if (kelasStr.isNotEmpty()) {
                val kelas = kelasStr.toInt()
                viewModel.setTempKelas(kelas)

                lifecycleScope.launch {
                    try {
                        val nama = viewModel.getTempNama()
                        val email = viewModel.getTempEmail()
                        Log.d("KelasActivity", "Clicked btnCreate: nama=${nama}, email=${email}, kelas=${kelas}")

                        if (nama == null || email == null) {
                            Toast.makeText(this@KelasActivity, "Nama atau Email kosong!", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val apiService = ApiConfig.getApiService()
                        val response = apiService.register(RegisterRequest(nama, email, kelas))

                        if (response.message == "User dengan email $email berhasil dibuat") {
                            viewModel.saveUserAfterRegister() // Save to datastore after successful registration
                            startActivity(Intent(this@KelasActivity, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@KelasActivity, "Gagal register: ${response.message}", Toast.LENGTH_SHORT).show()
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