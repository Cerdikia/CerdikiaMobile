package com.fhanafi.cerdikia.ui.completion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.databinding.FragmentCompletionBinding
import com.fhanafi.cerdikia.R
import kotlinx.coroutines.launch

class CompletionFragment : Fragment() {

    private var _binding: FragmentCompletionBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val xp = arguments?.getInt("XP") ?: 0
        val gems = arguments?.getInt("GEMS") ?: 0
        val materiId = arguments?.getInt("materiId") ?: -1
        val idMapel = arguments?.getInt("idMapel") ?: -1
        Log.d("StageFragment", "Received idMapel CompletionFragment: $idMapel")

        binding.tvXp.text = "$xp"

        @Suppress("DEPRECATION")
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            userViewModel.userData.collect { user ->
                val isAlreadyCompleted = user.completedMateriIds.contains(materiId)

                binding.btnCompletion.setOnClickListener {
                    if (materiId != -1) {
                        if (!isAlreadyCompleted) {
//                            userViewModel.addCompletedMateriId(materiId)
                            userViewModel.updateUserProgress(xp = 0, gems = gems, completedId = materiId)
                        }

                        viewLifecycleOwner.lifecycleScope.launch {
                            // 1. Update via API (incrementing)
                            userViewModel.updatePointAndRefresh(
                                xp = xp,
                                gems = if (isAlreadyCompleted) 0 else gems
                            )
                            val skor = xp * 10
                            userViewModel.postLogSiswa(
                                idModule = materiId, // assuming `materiId` is equivalent to module
                                idKelas = user.kelas,
                                idMapel = idMapel, // ← change this as needed (if you pass it through navArgs or save it globally)
                                skor = skor
                            )

                            // 2. Wait until GET (refresh) completes
                            val job = launch { userViewModel.refreshPointData() }
                            job.join() // Ensure it's completed before navigating

                            // 3. Navigate
                            setFragmentResult("requestKey", bundleOf("idMapel" to idMapel))
                            findNavController().popBackStack(R.id.stageFragment, false) // langsung kembali ke StageFragment
//                            findNavController().popBackStack()
//                            findNavController().popBackStack()
//                            findNavController().navigate(R.id.stageFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}