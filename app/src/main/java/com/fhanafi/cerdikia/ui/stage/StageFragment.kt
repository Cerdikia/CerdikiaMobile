package com.fhanafi.cerdikia.ui.stage

import androidx.fragment.app.viewModels
import android.os.Bundle
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
import com.fhanafi.cerdikia.data.pref.UserPreference
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
        adapter = MateriAdapter(emptyList()) { clickedView, materiItem ->
            showPopupModul(clickedView, materiItem.title, materiItem.description) {
                val bundle = Bundle().apply {
                    putInt("materiId", materiItem.id)
                }
                requireView().findNavController()
                    .navigate(R.id.action_stageFragment_to_soalFragment, bundle)
            }
        }
        recyclerView.adapter = adapter

        setupObserver()
        return root
    }

    private var isLoaded = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    private fun showPopupModul(anchorView: View, title: String, description: String, onMulaiClicked: () -> Unit) {
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

        popupWindow = PopupWindow(
            popupView,
            widthPx, // Set fixed width
            heightPx, // Set fixed height
            true
        )

        val tvJudul = popupView.findViewById<TextView>(R.id.tv_judul_modul)
        val tvDeskripsi = popupView.findViewById<TextView>(R.id.tv_deskripsi_modul)
        val btnMulai = popupView.findViewById<Button>(R.id.btn_mulai_popup)

        tvJudul.text = title
        tvDeskripsi.text = description
        btnMulai.setOnClickListener {
            popupWindow?.dismiss()
            popupWindow = null
            onMulaiClicked()
        }

        // Make sure the popup has a background to handle outside touches for dismissal
        popupWindow?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))

        // Show the popup below the anchor view
        popupWindow?.showAsDropDown(anchorView, -250, 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}