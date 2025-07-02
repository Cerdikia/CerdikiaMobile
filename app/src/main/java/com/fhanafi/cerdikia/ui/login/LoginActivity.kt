package com.fhanafi.cerdikia.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.mapper.toUserModel
import com.fhanafi.cerdikia.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initialBinding()
        auth = Firebase.auth
        supportActionBar?.hide()

        // Init UserViewModel
         authViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[AuthViewModel::class.java]

        setupListener()
        observeLoginState()
    }

    // Observe loading state login menggunakan sealed class UiState pattern yang khusus untuk MVI (Model-View-Intent) tetapi pattern MVVM juga bisa menggunakannya karena bisa lebih akurat untuk mengatur statenya
    private fun observeLoginState() {
        lifecycleScope.launch {
            authViewModel.isLoading.collect{ isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.btnLogin.isEnabled = !isLoading
            }
        }
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id)) // dari strings.xml
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    // Masih serasa ngeglitch saat masuk atau relogin ke aplikasi seperti balik ke LoginActivity dan masuk ke NamaActivity
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            lifecycleScope.launch {
                val email = currentUser.email ?: return@launch

                try {
                    // Early return on login error
                    val response = authViewModel.login(email) ?: return@launch
                    // Simpan hasil login ke DataStore
                    response.data?.let { data ->
                        val userModel = data.toUserModel()
                        authViewModel.saveUserData(userModel.nama, userModel.email, userModel.kelas) // nanti pindahin lagi karena ini hanya mencoba untuk menyatukan response APi agar tidak menggunakan authViewModel untuk save data user terpisah
                        authViewModel.saveUserTokens(userModel.accessToken, userModel.refreshToken)
                        userModel.photoUrl?.let { photoUrl ->
                            authViewModel.savePhotoUrl(photoUrl)
                        }
                    }

                    Log.d("LoginActivity", "Response: $response")
                    val userData = authViewModel.userData.first()
                    Log.d("LoginActivity", "User Data: $userData")
                    if (userData.nama.isEmpty()) {
                        // Kalau nama kosong berarti memang belum lengkap, lanjut ke NamaActivity
                        startActivity(Intent(this@LoginActivity, NamaActivity::class.java))
                    } else {
                        // Nama sudah ada → lanjut ke MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }
                    finish()

                } catch (e: HttpException) {
                    if (e.code() == 401) {
                        Log.d("LoginActivity", "Login failed with 401 - redirecting to NamaActivity")
                        navigateToNamaActivity(email)
                        finish()
                    } else {
                        e.printStackTrace()
                    }
                }catch (e: Exception){
                    navigateToNamaActivity(email)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun navigateToNamaActivity(email: String) {
        val intent = Intent(this@LoginActivity, NamaActivity::class.java)
        intent.putExtra("EXTRA_EMAIL", email)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val userData = authViewModel.userData.first()
            val currentUser = auth.currentUser

            // Jangan auto-login kalau data user kosong
            if (currentUser != null && userData.nama.isNotEmpty()) {
                updateUI(currentUser)
            }
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

    private fun initialBinding() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}