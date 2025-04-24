package com.fhanafi.cerdikia.ui.rangking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.UserViewModelFactory
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.databinding.FragmentRangkingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RangkingFragment : Fragment() {
    private var _binding: FragmentRangkingBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        UserViewModelFactory(UserPreference.getInstance(requireContext()))
    }

    private lateinit var rangkingViewModel: RangkingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRangkingBinding.inflate(inflater, container, false)
        rangkingViewModel = ViewModelProvider(this)[RangkingViewModel::class.java]

        setupRecyclerView()
        observeUserData()
        onBackButtonPressed()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvRangking.layoutManager = LinearLayoutManager(requireContext())

        rangkingViewModel.topPlayerRankingList.observe(viewLifecycleOwner) { list ->
            binding.rvRangking.adapter = RankingAdapter(list)
        }
    }

    private fun observeUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userData.collectLatest { user ->
                val currentList = rangkingViewModel.topPlayerRankingList.value ?: return@collectLatest

                // Add current user to the list, re-sort by XP descending, then re-assign ranks
                val dynamicList = (currentList.filterNot { it.isCurrentUser } + RankingItem(0, user.nama, user.xp, isCurrentUser = true))
                    .sortedByDescending { it.xp }
                    .mapIndexed { index, item -> item.copy(rank = index + 1) }

                binding.rvRangking.adapter = RankingAdapter(dynamicList)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
