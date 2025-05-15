package com.fhanafi.cerdikia.ui.stage

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.UserViewModel
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.helper.calculateRemainingTimeMillis
import com.fhanafi.cerdikia.databinding.FragmentStageBinding
import com.fhanafi.cerdikia.ui.loading.LoadingDialogFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
        _binding = FragmentStageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.recyclerViewMateri
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val idMapel = arguments?.getInt("idMapel") ?: -1
//        Log.d("StageFragment", "Received idMapel StageFragment: $idMapel")
        adapter = MateriAdapter(emptyList()) { clickedView, materiItem ->
            showPopupModul(clickedView, materiItem.title, materiItem.description, materiItem.id, idMapel, materiItem.isCompleted)
        }
        recyclerView.adapter = adapter
        loadData()
        setupObserver()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get arguments passed from HomeAdapter
        val namaMapel = arguments?.getString("namaMapel") ?: "Materi Title"
        val description = arguments?.getString("description") ?: "Materi Description"

        // Set them to the TextViews
        binding.textMateriTitle.text = namaMapel
        binding.textMateriDescription.text = description

        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { _, bundle ->
            val newIdMapel = bundle.getInt("idMapel", -1)
//            Log.d("StageFragment", "Received via FragmentResult: $newIdMapel")
            val newNamaMapel = bundle.getString("namaMapel")
            val newDescription = bundle.getString("description")

            if (!newNamaMapel.isNullOrEmpty()) {
                binding.textMateriTitle.text = newNamaMapel
            }
            if (!newDescription.isNullOrEmpty()) {
                binding.textMateriDescription.text = newDescription
            }

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
                launch {
                    viewModel.materiList.collect { materiList ->
                        adapter.setData(materiList)
                    }
                }
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        if (isLoading) {
                            showLoading()
                            delay(3000)
                        } else {
                            hideLoading()
                        }
                    }
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

            lifecycleScope.launch {
                val user = userViewModel.userData.first()

                if (user.energy <= 0) {
                    val energyData = viewModel.getEnergy(user.email)
                    val remainingMillis = calculateRemainingTimeMillis(energyData?.lastUpdated)
                    showEnergyDepletedPopup(remainingMillis)
                } else {
                    try {
                        viewModel.useEnergy(user.email)
                        userViewModel.refreshPointData()

                        val bundle = Bundle().apply {
                            putInt("materiId", materiId)
                            putInt("idMapel", idMapel)
                            putBoolean("isCompleted", isCompleted)
                        }

                        requireView().findNavController()
                            .navigate(R.id.action_stageFragment_to_soalFragment, bundle)
                    } catch (e: Exception) {
                        Log.e("StageFragment", "Failed to use energy: ${e.message}")
                    }
                }
            }
        }

        popupWindow?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
        popupWindow?.showAsDropDown(anchorView, -250, 8)
    }

    private fun showEnergyDepletedPopup(remainingMillis: Long) {
        val dialog = Dialog(requireContext())
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_energy, null)
        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)

        val timerTextView = dialogView.findViewById<TextView>(R.id.tvMessageTimer)
        val closeBtn = dialogView.findViewById<TextView>(R.id.textViewClose)

        val countDownTimer = object : CountDownTimer(remainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                dialog.dismiss()
            }
        }

        countDownTimer.start()

        closeBtn.setOnClickListener {
            countDownTimer.cancel()
            dialog.dismiss()
        }

        dialog.show()
        val widthInPx = (310 * resources.displayMetrics.density).toInt()
        val heightInPx = (197 * resources.displayMetrics.density).toInt()
        dialog.window?.setLayout(widthInPx, heightInPx)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}