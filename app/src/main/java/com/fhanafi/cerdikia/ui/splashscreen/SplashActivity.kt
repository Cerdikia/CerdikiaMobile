package com.fhanafi.cerdikia.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityLoginBinding
import com.fhanafi.cerdikia.databinding.ActivitySplashBinding
import com.fhanafi.cerdikia.ui.login.AuthViewModel
import com.fhanafi.cerdikia.ui.login.AuthViewModelFactory
import com.fhanafi.cerdikia.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this, AuthViewModelFactory(this))[AuthViewModel::class.java]
        observeUserRegistration()
    }

    private fun initialBinding(){
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun observeUserRegistration() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)

            viewModel.getNavigationTarget().collect { target ->
                startActivity(Intent(this@SplashActivity, target))
                finish()
            }
        }
    }
}