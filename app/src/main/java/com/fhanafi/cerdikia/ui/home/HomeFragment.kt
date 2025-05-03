package com.fhanafi.cerdikia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.PagerSnapHelper
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.ViewModelFactory
import com.fhanafi.cerdikia.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.combine

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var adapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var indicatorLayout: LinearLayout
    private var indicatorDots = mutableListOf<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView = binding.rvJudul
        recyclerView.layoutManager = layoutManager

        // Observe StateFlow
        @Suppress("DEPRECATION")
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            combine(
                homeViewModel.isLoading,
                homeViewModel.mapels
            ) { isLoading, mapels ->
                isLoading to mapels
            }.collect { (isLoading, mapels) ->
                if (isLoading) {
                    val shimmerCount = 3
                    recyclerView.adapter = HomeShimmerAdapter(itemCount = shimmerCount)
                    updateIndicatorDots(shimmerCount)
                    updatePageIndicatior(0) // ensure first dot is selected
                } else {
                    adapter = HomeAdapter(mapels)
                    recyclerView.adapter = adapter
                    updateIndicatorDots(mapels.size)
                }
            }
        }

        // Observe loading or error if needed
        homeViewModel.loadMapels(idKelas = 1, finished = false) // just change it to true if want to fetch finished mapel

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    updatePageIndicatior(currentPosition)
                }
            }
        })

        exitApp()
        return root
    }

    private fun updateIndicatorDots(itemCount: Int) {
        indicatorLayout = binding.indicatorLayout
        indicatorLayout.removeAllViews()
        indicatorDots.clear()

        for (i in 0 until itemCount) {
            val dot = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(8, 0, 8, 0) }
            dot.layoutParams = layoutParams
            dot.setImageResource(R.drawable.indicator_default)
            indicatorDots.add(dot)
            indicatorLayout.addView(dot)
        }

        if (indicatorDots.isNotEmpty()) {
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

    private fun exitApp() {
        val callback = object : OnBackPressedCallback(true) {
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