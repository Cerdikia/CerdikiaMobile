package com.fhanafi.cerdikia.ui.completion

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ActivityCompletionBinding

class CompletionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCompletionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val xp = intent.getIntExtra("XP", 0)
        val gems = intent.getIntExtra("GEMS", 0)

        binding.tvXp.text = "$xp"
        navigateToHome()
        //TODO: Bringing the xp and gems to user Data either in api or local storage
    }

    private fun navigateToHome(){
        binding.btnCompletion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}