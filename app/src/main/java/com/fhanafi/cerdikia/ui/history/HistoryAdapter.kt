package com.fhanafi.cerdikia.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val items = mutableListOf<HistoryItem>()

    fun submitList(newItems: List<HistoryItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryItem) {
            binding.tvTransactionId.text = item.transactionId
            binding.tvItemName.text = item.itemName
            binding.tvItemCount.text = "X ${item.itemCount}"
            binding.tvStatus.text = item.status
            binding.tvDateExchange.text = "Tanggal Penukaran: ${item.dateExchange}"
            Glide.with(binding.ivItemImage.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_loading)
                .error(R.drawable.placeholder_error)
                .into(binding.ivItemImage)

            // Handle click
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

data class HistoryItem(
    val transactionId: String,
    val itemName: String,
    val itemCount: Int,
    val status: String,
    val dateExchange: String,
    val imageUrl: String // Ganti dari imageResId ke imageUrl
)
