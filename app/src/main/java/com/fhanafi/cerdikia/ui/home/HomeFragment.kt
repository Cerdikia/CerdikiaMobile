package com.fhanafi.cerdikia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.PagerSnapHelper
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var indicatorLayout: LinearLayout
    private var indicatorDots = mutableListOf<ImageView>()
    private val placeHolderItem = listOf(
        HomePlaceHolderItem("Bagian 1 : Judul", "3 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
        HomePlaceHolderItem("Bagian 2 : Judul", "0 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
        HomePlaceHolderItem("Bagian 3 : Judul", "0 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
        HomePlaceHolderItem("Bagian 4 : Judul", "0 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
        HomePlaceHolderItem("Bagian 5 : Judul", "0 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
        HomePlaceHolderItem("Bagian 6 : Judul", "0 / 10 Materi", "Deskripsi dari bagian ini bisa dengan penjelasan apapun"),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        indicatorLayout = binding.indicatorLayout
        updateIndicatorDots(placeHolderItem.size)

        //setup recyclerview
        recyclerView = binding.rvJudul
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager

        adapter = HomeAdapter(placeHolderItem)
        recyclerView.adapter = adapter

        val pagerSnapHelper  = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val currentLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                    currentLayoutManager?.let {
                        val currentPosition = it.findFirstCompletelyVisibleItemPosition()
                        updatePageIndicatior(currentPosition)
                    }
                }
            }
        })
        exitApp()
        return root
    }

    private fun updateIndicatorDots(itemCount: Int) {
        indicatorLayout.removeAllViews() //clear existing dots
        indicatorDots.clear()

        for(i in 0 until itemCount){
            val dot = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8,0) // adjust margin as needed
            dot.layoutParams = layoutParams
            dot.layoutParams = layoutParams
            dot.setImageResource(R.drawable.indicator_default) // Set default state
            indicatorDots.add(dot)
            indicatorLayout.addView(dot)
        }

        if(indicatorDots.isNotEmpty()){
            indicatorDots[0].setImageResource(R.drawable.indicator_selected)
        }
    }

    private fun updatePageIndicatior(position: Int) {
        indicatorDots.forEachIndexed { index, imageView ->
            imageView.setImageResource(
                if (index == position) R.drawable.indicator_selected else R.drawable.indicator_default
            )
        }
    }

    private fun exitApp(){
        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}