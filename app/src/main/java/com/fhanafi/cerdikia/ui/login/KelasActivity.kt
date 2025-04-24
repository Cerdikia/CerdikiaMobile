package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityKelasBinding

class KelasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelasBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]
        setupListener()
    }
    // TODO: Mencheck di edit text dengan helper untuk mengecek inputan kelas agar mendapat nilai kelas yang pasti satu digit dan tidak diawali dengan 0
    private fun setupListener(){
        binding.btnCreate.setOnClickListener {
            val kelas = binding.etKelas.text.toString()
            if(kelas.isNotEmpty()){
                viewModel.saveKelas(kelas.toInt())
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun initialBinding(){
        binding = ActivityKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}