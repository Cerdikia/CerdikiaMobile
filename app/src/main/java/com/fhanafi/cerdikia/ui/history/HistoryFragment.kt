package com.fhanafi.cerdikia.ui.history

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentHistoryBinding
import com.fhanafi.cerdikia.databinding.FragmentProfileBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        //Hide bottom navigation
        (activity as? MainActivity)?.setBottomNavigationVisibility(false)
        navigateToProfile()
        return binding.root
    }

    private fun navigateToProfile() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_profileFragment)
        }
    }
}