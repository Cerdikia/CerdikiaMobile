package com.fhanafi.cerdikia.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.FragmentProfileBinding
import com.fhanafi.cerdikia.helper.SessionManager
import com.fhanafi.cerdikia.helper.uriToFile
import com.fhanafi.cerdikia.ui.loading.LoadingDialogFragment
import com.fhanafi.cerdikia.ui.splashscreen.SplashActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
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

    private var loadingDialog: LoadingDialogFragment? = null
    private var isUpdating = false
    private var isUploading = false

    private fun updateLoadingState() {
        if (isUpdating || isUploading) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogFragment()
            loadingDialog?.show(parentFragmentManager, "loading")
        }
    }

    private fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        onBackButtonPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleSignInClient()
        setupUI()
        observeViewModel()
        history()
    }

    private fun history() {
        binding.btnRiwayat.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_historyFragment)
        }
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

        userViewModel.isUpdating.observe(viewLifecycleOwner) { updating ->
            isUpdating = updating
            updateLoadingState()

            binding.tvSimpan.isEnabled = !updating
            binding.tvSimpan.alpha = if (updating) 0.5f else 1f

            if (!updating && !isFirstLoad) {
                Toast.makeText(requireContext(), "Profile berhasil diperbarui!", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.navigation_home)
            }
            isFirstLoad = false
        }

        userViewModel.isUploading.observe(viewLifecycleOwner) { uploading ->
            isUploading = uploading
            updateLoadingState()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            showLoading()
            userViewModel.userData.collect { user ->
                delay(2000)
                binding.editTextNama.setText(user.nama)
                binding.editTextEmail.setText(user.email)
                binding.editTextKelas.setText(user.kelas.toString())

                Glide.with(this@ProfileFragment)
                    .load(user.photoUrl)
                    .placeholder(R.drawable.player_holder)
                    .error(R.drawable.player_holder)
                    .circleCrop()
                    .into(binding.viewProfilePlaceholder)
                hideLoading()
            }
        }
    }

    private fun onBackButtonPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigation_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setBottomNavigationVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}