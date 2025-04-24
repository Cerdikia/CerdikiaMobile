package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityNamaBinding

class NamaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNamaBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]

        setupListener()
    }

    private fun setupListener(){
        binding.btnNext.setOnClickListener {
            val nama = binding.etNama.text.toString()
            if(nama.isNotEmpty()){
                viewModel.saveNama(nama)
                startActivity(Intent(this, KelasActivity::class.java))
                finish()
            }else{
                binding.etNama.error = getString(R.string.nama_kosong)
            }
        }
    }

    private fun initialBinding(){
        binding = ActivityNamaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}