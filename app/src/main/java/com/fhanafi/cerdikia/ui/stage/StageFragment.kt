package com.fhanafi.cerdikia.ui.stage

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.databinding.FragmentStageBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StageFragment : Fragment() {

    private var _binding: FragmentStageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StageViewModel by activityViewModels{
        ViewModelFactory.getInstance(requireContext())
    }
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
        val idMapel = arguments?.getInt("idMapel") ?: -1
        Log.d("StageFragment", "Received idMapel StageFragment: $idMapel")
        adapter = MateriAdapter(emptyList()) { clickedView, materiItem ->
            showPopupModul(clickedView, materiItem.title, materiItem.description, materiItem.id, idMapel, materiItem.isCompleted)
        }
        recyclerView.adapter = adapter
        loadData()
        setupObserver()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
            val newIdMapel = bundle.getInt("idMapel", -1)
            Log.d("StageFragment", "Received via FragmentResult: $newIdMapel")
            if (newIdMapel != -1) {
                viewLifecycleOwner.lifecycleScope.launch {
                    userViewModel.userData.collectLatest { user ->
                        viewModel.loadMateri(newIdMapel, user.kelas, user.email)
                    }
                }
            }
        }
    }

    private var isLoaded = false
    private fun loadData(){
        val idMapel = arguments?.getInt("idMapel") ?: return
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userData.collectLatest { user ->
                if (!isLoaded) {
                    viewModel.loadMateri(idMapel, user.kelas, user.email)
                    isLoaded = true
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setupObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.materiList.collect { materiList ->
                    adapter.setData(materiList)
                }
            }
        }
    }

    private var popupWindow: PopupWindow? = null

    private fun showPopupModul(anchorView: View, title: String, description: String, materiId: Int, idMapel: Int, isCompleted: Boolean) {
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_materi_info, null)
        val widthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            250f,
            resources.displayMetrics
        ).toInt()

        val heightPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            180f,
            resources.displayMetrics
        ).toInt()

        popupWindow = PopupWindow(popupView, widthPx, heightPx, true)

        val tvJudul = popupView.findViewById<TextView>(R.id.tv_judul_modul)
        val tvDeskripsi = popupView.findViewById<TextView>(R.id.tv_deskripsi_modul)
        val btnMulai = popupView.findViewById<Button>(R.id.btn_mulai_popup)

        tvJudul.text = title
        tvDeskripsi.text = description

        btnMulai.setOnClickListener {
            popupWindow?.dismiss()
            popupWindow = null

            val bundle = Bundle().apply {
                putInt("materiId", materiId)
                putInt("idMapel", idMapel)
                putBoolean("isCompleted", isCompleted)
            }
            requireView().findNavController()
                .navigate(R.id.action_stageFragment_to_soalFragment, bundle)
        }

        popupWindow?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
        popupWindow?.showAsDropDown(anchorView, -250, 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}