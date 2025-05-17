package com.fhanafi.cerdikia.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.MainActivity
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.mapper.toHistoryItem
import com.fhanafi.cerdikia.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyViewModel: HistoryViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryAdapter{ historyItem ->
            historyViewModel.fetchReceiptHtml(historyItem.transactionId)
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        // Observe history list
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            historyViewModel.historyList.collect { list ->
                val mapped = list.map { it.toHistoryItem() }
                adapter.submitList(mapped)
            }
        }

        // Optional: observe error message
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            historyViewModel.errorMessage.collect { error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            historyViewModel.receiptHtml.collect { html ->
                html?.let {
                    val action = HistoryFragmentDirections
                        .actionHistoryFragmentToReceiptWebViewFragment(it)
                    findNavController().navigate(action)
                    historyViewModel.clearReceiptHtml()
                }
            }
        }
        // Fetch data from API
        if (historyViewModel.historyList.value.isEmpty()) {
            historyViewModel.fetchHistory()
        }
    }

    private fun navigateToProfile() {
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_profileFragment)
        }
    }
}