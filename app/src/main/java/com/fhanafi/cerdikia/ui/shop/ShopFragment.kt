package com.fhanafi.cerdikia.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentShopBinding
import androidx.recyclerview.widget.LinearLayoutManager


class ShopFragment : Fragment() {

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // In your Fragment/Adapter
        // Get a reference to the RecyclerView from the binding
        val recyclerViewMisi = binding.recyclerViewMisi
        recyclerViewMisi.layoutManager = LinearLayoutManager(requireContext())

        val recycleViewToko = binding.recyclerViewToko
        recycleViewToko.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val tokoItems = listOf(
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            Toko(R.drawable.img_pencil, 1000),
            // Add more toko items here if needed
        )

        // Sample data for Misi Harian
        val misiList = listOf(
            MisiHarian(R.drawable.ic_coin, "Dapatkan 50 XP", 45, 50),
            // Add more misi items here if needed
        )

        val adapterToko = TokoAdapter(tokoItems)
        recycleViewToko.adapter = adapterToko

        val adapterMisi = MisiHarianAdapter(misiList)
        recyclerViewMisi.adapter = adapterMisi

        onBackButtonPressed()
        return root
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
        _binding = null
    }
}