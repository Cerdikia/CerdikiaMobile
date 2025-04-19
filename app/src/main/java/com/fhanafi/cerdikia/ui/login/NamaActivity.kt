package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityNamaBinding

class NamaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNamaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        setupListener()
    }

    private fun setupListener(){
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, KelasActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initialBinding(){
        binding = ActivityNamaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}