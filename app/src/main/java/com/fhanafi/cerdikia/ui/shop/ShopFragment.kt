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
import com.fhanafi.cerdikia.helper.DailyQuestUtils

class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding
    private var xpRewardClaimed = false
    private var quizRewardClaimed = false
    private var studyTimeRewardClaimed = false
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val shopViewModel: ShopViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        // In your Fragment/Adapter
        // Get a reference to the RecyclerView from the binding
//        val recyclerViewMisi = binding.recyclerViewMisi
//        recyclerViewMisi.layoutManager = LinearLayoutManager(requireContext())

        val recycleViewToko = binding!!.recyclerViewToko
        recycleViewToko.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val tokoItems = listOf(
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            // Add more toko items here if needed
        )

        val adapterToko = TokoAdapter(tokoItems)
        recycleViewToko.adapter = adapterToko

//        adapter = MisiHarianAdapter()
//        binding.recyclerViewMisi.adapter = adapter
//        binding.recyclerViewMisi.layoutManager = LinearLayoutManager(requireContext())
        shopViewModel.checkAndResetQuestIfNeeded()
        observeDailyQuest()
        onBackButtonPressed()
        return root
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

                if (quest.xpEarned >= 50 && !xpRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(10)
                    xpRewardClaimed = true
                }

                // Quiz Mission
                currentBinding.progressQuiz.max = 2
                currentBinding.progressQuiz.progress = quest.quizzesCompleted
                currentBinding.tvProgressQuiz.text = if (quest.quizRewardClaimed) {
                    "Complete"
                } else {
                    "${quest.quizzesCompleted} / 2"
                }

                if (quest.quizzesCompleted >= 2 && !quizRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(5)
                    quizRewardClaimed = true
                }

                // Study Time Mission
                currentBinding.progressTime.max = 30
                currentBinding.progressTime.progress = quest.minutesStudied
                currentBinding.tvProgressTime.text = if (quest.studyTimeRewardClaimed) {
                    "Complete"
                } else {
                    "${quest.minutesStudied} / 30"
                }

                if (quest.minutesStudied >= 30 && !studyTimeRewardClaimed) {
                    userViewModel.updateGemsFromMissionReward(10)
                    studyTimeRewardClaimed = true
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