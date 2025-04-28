package com.fhanafi.cerdikia.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.ActivitySplashBinding
import com.fhanafi.cerdikia.ui.login.AuthViewModel
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: AuthViewModel
    private val splashScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]

        splashScope.launch {
            delay(3000)
            val targetActivity = viewModel.getNavigationTarget() // sekarang langsung Class
            startActivity(Intent(this@SplashActivity, targetActivity))
            finish()
        }
    }

    private fun initialBinding(){
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        splashScope.cancel() // pastiin tidak ada memory leak
    }
}