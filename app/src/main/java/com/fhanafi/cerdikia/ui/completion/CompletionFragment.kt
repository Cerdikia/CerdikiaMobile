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
import com.fhanafi.cerdikia.ui.loading.LoadingDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CompletionFragment : Fragment() {

    private var _binding: FragmentCompletionBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
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
        val isAlreadyCompleted = arguments?.getBoolean("isCompleted") ?: false

        binding.tvXp.text = "$xp"

        @Suppress("DEPRECATION")
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            userViewModel.userData.collect { user ->
//                val localCompleted = user.completedMateriIds.contains(materiId)
//                val isAlreadyCompleted = remoteCompleted || localCompleted // i want to more defensive  to check each other

                binding.btnCompletion.setOnClickListener {
                    if (materiId != -1) {
                        if (!isAlreadyCompleted) {
                            userViewModel.updateUserProgress(
                                xp = 0,
                                gems = gems,
                                completedId = materiId //
                            )
                        }

                        viewLifecycleOwner.lifecycleScope.launch {
                            showLoading()
                            // 1. Update via API (incrementing)
                            userViewModel.updatePointAndRefresh(
                                xp = xp,
                                gems = if (isAlreadyCompleted) 0 else gems
                            )

                            val skor = xp * 10
                            userViewModel.postLogSiswa(
                                idModule = materiId,
                                idKelas = user.kelas,
                                idMapel = idMapel,
                                skor = skor
                            )
                            // 2. Wait until GET (refresh) completes
                            val job = launch { userViewModel.refreshPointData() }
                            job.join() // Ensure it's completed before navigating
                            binding.main.visibility = View.GONE
                            delay(3000)
                            hideLoading()
                            // 3. Navigate
                            setFragmentResult("requestKey", bundleOf("idMapel" to idMapel))
                            findNavController().popBackStack(R.id.stageFragment, false)
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

