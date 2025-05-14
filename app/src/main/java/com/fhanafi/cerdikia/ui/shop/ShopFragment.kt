package com.fhanafi.cerdikia.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentShopBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.CountDownTimer
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem
import com.fhanafi.cerdikia.helper.DailyQuestUtils
import com.fhanafi.cerdikia.helper.OnShopItemInteractionListener
import com.fhanafi.cerdikia.ui.loading.LoadingDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ShopFragment : Fragment(), OnShopItemInteractionListener {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val shopViewModel: ShopViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var countDownTimer: CountDownTimer? = null
    private var loadingDialog: LoadingDialogFragment? = null

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        observeLoadingState()
        shopViewModel.loadHadiahList()
        shopViewModel.checkAndResetQuestIfNeeded()
        observeDailyQuest()
        observeHadiahList()
        onBackButtonPressed()
        return root
    }
    // Implement the methods from OnShopItemInteractionListener
    private fun updateExchangeButtonVisibility() {
        val adapter = binding?.recyclerViewToko?.adapter as? TokoAdapter
        val isAnyItemSelected = adapter?.itemCountsRv?.any { it.value > 0 } ?: false
        binding?.buttonTukarkan?.visibility = if (isAnyItemSelected) View.VISIBLE else View.GONE
        binding?.buttonBatal?.visibility = if (isAnyItemSelected) View.VISIBLE else View.GONE
    }

    override fun onItemBought(item: HadiahDataItem) {
        updateExchangeButtonVisibility()
    }

    override fun onItemCountChanged(item: HadiahDataItem, quantity: Int) {
        updateExchangeButtonVisibility()
    }

    private fun observeHadiahList() {
        val recycleViewToko = binding!!.recyclerViewToko
        recycleViewToko.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Observe the list of gifts from ShopViewModel
        shopViewModel.hadiahList.onEach { tokoList ->
            // Update RecyclerView adapter with the new list of gifts
            val adapterToko = TokoAdapter(tokoList, this)
            recycleViewToko.adapter = adapterToko
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeLoadingState() {
        shopViewModel.isLoading.onEach { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeDailyQuest() {
        lifecycleScope.launch {
            shopViewModel.dailyQuest.collect { quest ->
                val currentBinding = _binding ?: return@collect

                // Cancel previous timer if any
                countDownTimer?.cancel()

                // Countdown
                val timeLeftMillis = DailyQuestUtils.timeUntilNextReset(quest.lastResetTime)
                countDownTimer = DailyQuestUtils.startCountdownTimer(timeLeftMillis, currentBinding.tvTimer)

                // XP Mission
                currentBinding.progressXP.max = 50
                currentBinding.progressXP.progress = quest.xpEarned
                currentBinding.tvProgress.text = if (quest.xpRewardClaimed) {
                    "Complete"
                } else {
                    "${quest.xpEarned} / 50"
                }

                if (quest.xpEarned >= 50 && !quest.xpRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(10)
                    shopViewModel.setXpRewardClaimed(true)
                }

                // Quiz Mission
                currentBinding.progressQuiz.max = 2
                currentBinding.progressQuiz.progress = quest.quizzesCompleted
                currentBinding.tvProgressQuiz.text = if (quest.quizRewardClaimed) {
                    "Complete"
                } else {
                    "${quest.quizzesCompleted} / 2"
                }
                if (quest.quizzesCompleted >= 2 && !quest.quizRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(5)
                    shopViewModel.setQuizRewardClaimed(true)
                }
                // Study Time Mission
                currentBinding.progressTime.max = 30
                currentBinding.progressTime.progress = quest.minutesStudied
                currentBinding.tvProgressTime.text = if (quest.studyTimeRewardClaimed) {
                    "Complete"
                } else {
                    "${quest.minutesStudied} / 30"
                }

                if (quest.minutesStudied >= 30 && !quest.studyTimeRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(10)
                    shopViewModel.setStudyTimeRewardClaimed(true)
                }
            }
        }
    }

    private fun onBackButtonPressed(){
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.navigation_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}