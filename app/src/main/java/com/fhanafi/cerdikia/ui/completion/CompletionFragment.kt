package com.fhanafi.cerdikia.ui.completion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

        binding.btnCompletion.setOnClickListener {
            if (materiId != -1) {
                userViewModel.addCompletedMateriId(materiId)
                userViewModel.updateUserProgress(xp, gems, materiId)
            }

            findNavController().popBackStack(R.id.stageFragment, false) // Back to StageFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}