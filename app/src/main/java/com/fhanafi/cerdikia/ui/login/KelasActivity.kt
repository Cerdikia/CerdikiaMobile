package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityKelasBinding

class KelasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKelasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        setupListener()
    }
    // TODO: Mencheck di edit text dengan helper untuk mengecek inputan kelas agar mendapat nilai kelas yang pasti
    private fun setupListener(){
        binding.btnCreate.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initialBinding(){
        binding = ActivityKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}