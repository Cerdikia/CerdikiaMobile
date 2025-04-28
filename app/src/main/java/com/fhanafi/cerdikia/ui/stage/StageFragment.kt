package com.fhanafi.cerdikia.ui.stage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.data.pref.UserPreference
import com.fhanafi.cerdikia.databinding.FragmentStageBinding

class StageFragment : Fragment() {

    companion object {
        fun newInstance() = StageFragment()
    }
    private var _binding: FragmentStageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StageViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: MateriAdapter

    //TODO: buat arrow_up dan judul dari materi yang diperlihatkan yang berfungsi untuk menclose recycleView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerViewMateri
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MateriAdapter(emptyList()) // Initialize with an empty list
        recyclerView.adapter = adapter

        viewModel.materiList.observe(viewLifecycleOwner, Observer { materiItems ->
            adapter.setData(materiItems) // If using ListAdapter

        })

        setupObserver()
        return root
    }

    @Suppress("DEPRECATION")
    private fun setupObserver(){
        lifecycleScope.launchWhenStarted {
            userViewModel.userData.collect { userModel ->
                val completedIds = userModel.completedMateriIds
                val updatedList = viewModel.materiList.value?.map {
                    it.copy(isCompleted = completedIds.contains(it.id))
                } ?: emptyList()
                adapter.setData(updatedList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}