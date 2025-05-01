package com.fhanafi.cerdikia.ui.rangking

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ItemRangkingBinding

class RankingAdapter(private val rankingList: List<RankingItem>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    inner class RankingViewHolder(binding: ItemRangkingBinding) : RecyclerView.ViewHolder(binding.root) {
        val rankTextView = binding.textViewRank
        val profilePlaceholderView = binding.viewProfilePlaceholder
        val playerNameTextView = binding.textViewPlayerName
        val xpTextView = binding.textViewXP
        val rootLayout = binding.rangkingItemRoot

        fun bind(item: RankingItem) {
            rankTextView.text = item.rank.toString()
            playerNameTextView.text = item.playerName
            xpTextView.text = "${item.xp} XP"
            Log.d("RankingPhoto", "Player photoUrl: ${item.photoUrl}")
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .placeholder(R.drawable.player_holder)
                .error(R.drawable.player_holder) // Optional: show a default image
                .circleCrop()
                .into(profilePlaceholderView)
            if(item.isCurrentUser){
                rootLayout.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.highlightBlue))
            }else{
                rootLayout.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            }
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
