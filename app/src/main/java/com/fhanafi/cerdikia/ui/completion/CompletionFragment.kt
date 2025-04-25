package com.fhanafi.cerdikia.ui.completion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.databinding.FragmentCompletionBinding
import com.fhanafi.cerdikia.R

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

        binding.tvXp.text = "$xp"

        @Suppress("DEPRECATION")
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            userViewModel.userData.collect { user ->
                val isAlreadyCompleted = user.completedMateriIds.contains(materiId)

                binding.btnCompletion.setOnClickListener {
                    if (materiId != -1) {
                        // Always give XP
                        userViewModel.updateUserProgress(xp = xp, gems = 0, completedId = materiId)

                        if (!isAlreadyCompleted) {
                            // Only give gems if not completed before
                            userViewModel.addCompletedMateriId(materiId)
                            userViewModel.updateUserProgress(xp = 0, gems = gems, completedId = materiId)
                        }
                    }

                    findNavController().popBackStack()
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.stageFragment)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}