package com.fhanafi.cerdikia.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityLoginBinding
import com.fhanafi.cerdikia.databinding.ActivitySplashBinding
import com.fhanafi.cerdikia.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()
        setupSplashScreen()
    }

    private fun initialBinding(){
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupSplashScreen() {
        val splashDelay: Long = 3000 // 3 seconds
        CoroutineScope(Dispatchers.Main).launch {
            delay(splashDelay)
            startLoginActivity()
            finish()
        }
    }

    private fun startLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }
}