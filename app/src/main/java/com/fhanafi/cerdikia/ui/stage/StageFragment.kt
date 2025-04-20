package com.fhanafi.cerdikia.ui.stage

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.MainViewModel
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentStageBinding

class StageFragment : Fragment() {

    companion object {
        fun newInstance() = StageFragment()
    }
    private var _binding: FragmentStageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StageViewModel by viewModels()
    private lateinit var adapter: MateriAdapter

    //TODO: #Prioritas di setiap materi misal materi pertama di recycleView menggunakan item_materi yang pertama yang berbentuk C dan materi yang kedua menggunakan item_materi2 yang berbentuk c terbalik
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
//             If not using ListAdapter, you might have adapter.setData(materiItems)

            // Set data ke CardView atas dari item pertama
            // kemungkinan berubah karena tidak ada stage per materi hanya ada
            if (materiItems.isNotEmpty()) {
                val firstItem = materiItems[0]
                binding.textMateriTitle.text = firstItem.title
                binding.textMateriDescription.text = firstItem.description
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}