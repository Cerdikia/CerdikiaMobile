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
import com.fhanafi.cerdikia.MainViewModel
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.databinding.FragmentRangkingBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RangkingFragment : Fragment() {
    private var _binding: FragmentRangkingBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val rangkingViewModel: RangkingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val _rankingLoaded = MutableStateFlow(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRangkingBinding.inflate(inflater, container, false)

        setupRecyclerView()
        onBackButtonPressed()
        rangkingViewModel.fetchRankingForUser() // <- Refetch when fragment is opened

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        badgeVisibiliy()
        observeUserDataAndUnlockNavigation()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun badgeVisibiliy(){
        binding.imgRangking.visibility = View.GONE
        binding.tvRangkingTitle.visibility = View.GONE
        binding.tvRangkingDescription.visibility = View.GONE
        binding.tvRangkingDay.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        binding.rvRangking.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            rangkingViewModel.isLoading.collectLatest { loading ->
                if (loading) {
                    binding.rvRangking.adapter = ShimmerAdapter()
                } else {
                    rangkingViewModel.topPlayerRankingList.collectLatest { list ->
                        binding.rvRangking.adapter = RankingAdapter(list)
                        _rankingLoaded.value = true
                    }
                }
            }
        }
    }

    private fun observeUserDataAndUnlockNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                userViewModel.userData,
                _rankingLoaded
            ) { user, isRankingLoaded -> user to isRankingLoaded }
                .collectLatest { (user, isRankingLoaded) ->
                    if (!isRankingLoaded) return@collectLatest

                    // Stop shimmer
                    binding.shimmerRankingHeader.stopShimmer()
                    binding.shimmerRankingHeader.visibility = View.GONE

                    // Show UI
                    binding.imgRangking.visibility = View.VISIBLE
                    binding.tvRangkingTitle.visibility = View.VISIBLE
                    binding.tvRangkingDescription.visibility = View.VISIBLE
                    binding.tvRangkingDay.visibility = View.VISIBLE

                    // Determine badge
                    val (badgeResId, title) = when {
                        user.xp >= 5000 -> R.drawable.ic_diamondrank to "Diamond League"
                        user.xp >= 3000 -> R.drawable.ic_platinumrank to "Platinum League"
                        user.xp >= 2000 -> R.drawable.ic_goldrangking to "Gold League"
                        user.xp >= 1000 -> R.drawable.ic_silverrank to "Silver League"
                        else -> R.drawable.ic_bronzerank to "Bronze League"
                    }

                    binding.imgRangking.setImageResource(badgeResId)
                    binding.tvRangkingTitle.text = title

                    // Add user to list and update adapter
                    val currentList = rangkingViewModel.topPlayerRankingList.value ?: return@collectLatest
                    val dynamicList = (currentList.filterNot { it.isCurrentUser } +
                            RankingItem(0, user.nama, user.xp, isCurrentUser = true, photoUrl = user.photoUrl))
                        .sortedByDescending { it.xp }
                        .mapIndexed { index, item -> item.copy(rank = index + 1) }

                    binding.rvRangking.adapter = RankingAdapter(dynamicList)

                    mainViewModel.setNavigationAllowed(true)
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
