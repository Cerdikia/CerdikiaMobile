package com.fhanafi.cerdikia.ui.rangking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.databinding.FragmentRangkingBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RangkingFragment : Fragment() {
    private var _binding: FragmentRangkingBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val rangkingViewModel: RangkingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRangkingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeUserData()
        onBackButtonPressed()
        rangkingViewModel.fetchRankingForUser() // <- Refetch when fragment is opened

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvRangking.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            rangkingViewModel.topPlayerRankingList.collectLatest { list ->
                binding.rvRangking.adapter = RankingAdapter(list)
            }
        }
    }

    private fun observeUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userData.collectLatest { user ->
                // set badge and title based on xp
                val (badgeResId, title) = when {
                    user.xp >= 5000 -> R.drawable.ic_diamondrank to "Diamond League"
                    user.xp >= 3000 -> R.drawable.ic_platinumrank to "Platinum League"
                    user.xp >= 2000 -> R.drawable.ic_goldrangking to "Gold League"
                    user.xp >= 1000 -> R.drawable.ic_silverrank to "Silver League"
                    else -> R.drawable.ic_bronzerank to "Bronze League"
                }

                binding.imgRangking.setImageResource(badgeResId)
                binding.tvRangkingTitle.text = title

                // update rangking list maybe it change if there has API implementation
                val currentList = rangkingViewModel.topPlayerRankingList.value ?: return@collectLatest
                Log.d("RankingPhoto", "Before insert: user photoUrl = ${user.photoUrl}")
                // Add current user to the list, re-sort by XP descending, then re-assign ranks
                val dynamicList = (currentList.filterNot { it.isCurrentUser } + RankingItem(0, user.nama, user.xp, isCurrentUser = true, photoUrl = user.photoUrl))
                    .sortedByDescending { it.xp }
                    .mapIndexed { index, item -> item.copy(rank = index + 1, photoUrl = item.photoUrl, isCurrentUser = item.isCurrentUser) }
                Log.d("RankingPhoto", "User photoUrl: ${user.photoUrl}")
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
