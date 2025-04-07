package com.fhanafi.cerdikia.ui.rangking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.FragmentRangkingBinding

//TODO: fix the night mode which the fragment layout is still white even is already swicthed to night mode (Rangking & Shop Fragment)
class RangkingFragment : Fragment() {
    private var _binding: FragmentRangkingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Dummy data for Top 5 Player
    private val TopPlayerRangkingList = listOf(
        RankingItem(1, "Player 1", 5000),
        RankingItem(2, "Player 2", 4009),
        RankingItem(3, "Player 3", 4007),
        RankingItem(4, "Player 4", 4004),
        RankingItem(5, "Player 5", 4002)
    )
    // Dummy data for Your Ranking
    private val PlayerRangkingList = listOf(
        RankingItem(100, "Ini Saya", 500, isCurrentUser = true),
        RankingItem(101, "Player 2", 409),
        RankingItem(102, "Player 3", 407),
        RankingItem(103, "Player 4", 404),
        RankingItem(104, "Player 5", 402),
        RankingItem(105, "Player 6", 401),
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rangkingViewModel =
            ViewModelProvider(this).get(RangkingViewModel::class.java)

        _binding = FragmentRangkingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //TODO: fix the top bar issue it still has delay effect which i want to create custom top bar using compose
        // Hide the ActionBar
//        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        // Setup RecyclerView for top rankings
        binding.rvRangking.apply { // Use rvRangking (lowercase)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RankingAdapter(TopPlayerRangkingList)
        }

        // Setup RecyclerView for your rankings
        binding.rvYourRangking.apply { // Use rvYourRangking (lowercase)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = RankingAdapter(PlayerRangkingList)
        }

        return root
    }

    /*override fun onStop() {
        super.onStop()
        (activity as? AppCompatActivity)?.supportActionBar?.show() // still buggy
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}