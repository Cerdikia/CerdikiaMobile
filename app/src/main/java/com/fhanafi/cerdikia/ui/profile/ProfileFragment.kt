package com.fhanafi.cerdikia.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.FragmentProfileBinding
import com.fhanafi.cerdikia.helper.SessionManager
import com.fhanafi.cerdikia.ui.login.AuthViewModel
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
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient

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
    }

    private fun logout() {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            FirebaseAuth.getInstance().signOut()
            SessionManager.clearSession(requireContext())

            Log.d("Logout", "userViewModel.clearData() and authViewModel.clearData() called")
            Toast.makeText(requireContext(), "Anda berhasil keluar!", Toast.LENGTH_SHORT).show()

            requireActivity().finishAffinity()
            exitProcess(0)
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
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}