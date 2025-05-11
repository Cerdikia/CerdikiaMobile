package com.fhanafi.cerdikia.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.FragmentProfileBinding
import com.fhanafi.cerdikia.helper.SessionManager
import com.fhanafi.cerdikia.helper.uriToFile
import com.fhanafi.cerdikia.ui.splashscreen.SplashActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val file = uriToFile(it, requireContext())
                val email = binding.editTextEmail.text.toString()
                userViewModel.uploadProfileImage(email, file)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleSignInClient()
        setupUI()
        observeViewModel()
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }
    // TODO: showing the profile picture from firebase
    private fun setupUI() {
        binding.tvSimpan.setOnClickListener {
            val nama = binding.editTextNama.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()
            val kelasString = binding.editTextKelas.text.toString().trim()

            if (nama.isEmpty() || email.isEmpty() || kelasString.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kelas = kelasString.toIntOrNull()
            if (kelas == null) {
                Toast.makeText(requireContext(), "Kelas harus berupa angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.updateUserProfile(nama, email, kelas)
            findNavController().navigate(R.id.navigation_home)
        }

        binding.btnKeluar.setOnClickListener {
            logout()
        }
        binding.tvChangeProfile.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private fun logout() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            FirebaseAuth.getInstance().signOut()
            SessionManager.clearSession(requireContext())

            Log.d("Logout", "userViewModel.clearData() and authViewModel.clearData() called")
            Toast.makeText(requireContext(), "Anda berhasil keluar!", Toast.LENGTH_SHORT).show()

            requireActivity().finishAffinity()
            val intent = Intent(requireContext(), SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        var isFirstLoad = true

        userViewModel.isUpdating.observe(viewLifecycleOwner) { isUpdating ->
            binding.tvSimpan.isEnabled = !isUpdating
            binding.tvSimpan.alpha = if (isUpdating) 0.5f else 1f
            binding.progressBar.visibility = if (isUpdating) View.VISIBLE else View.GONE

            if (!isUpdating && !isFirstLoad) {
                Toast.makeText(requireContext(), "Profile berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            }
            isFirstLoad = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userData.collect { user ->
                binding.editTextNama.setText(user.nama)
                binding.editTextEmail.setText(user.email)
                binding.editTextKelas.setText(user.kelas.toString())

                Glide.with(this@ProfileFragment)
                    .load(user.photoUrl)
                    .placeholder(R.drawable.player_holder)
                    .error(R.drawable.player_holder)
                    .circleCrop()
                    .into(binding.viewProfilePlaceholder)
            }
        }
        userViewModel.isUploading.observe(viewLifecycleOwner) { uploading ->
            binding.progressBar.visibility = if (uploading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}