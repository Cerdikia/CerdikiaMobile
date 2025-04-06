package com.fhanafi.cerdikia.ui.rangking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.databinding.ItemRangkingBinding

class RankingAdapter(private val rankingList: List<RankingItem>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    inner class RankingViewHolder(binding: ItemRangkingBinding) : RecyclerView.ViewHolder(binding.root) {
        val rankTextView = binding.textViewRank
        val profilePlaceholderView = binding.viewProfilePlaceholder
        val playerNameTextView = binding.textViewPlayerName
        val xpTextView = binding.textViewXP

        fun bind(item: RankingItem) {
            rankTextView.text = item.rank.toString()
            playerNameTextView.text = item.name
            xpTextView.text = "${item.xp} XP"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemRangkingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val currentItem = rankingList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }
}
