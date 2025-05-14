package com.fhanafi.cerdikia.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.data.remote.response.HadiahDataItem
import com.fhanafi.cerdikia.helper.OnShopItemInteractionListener

class TokoAdapter(
    private val tokoList: List<HadiahDataItem>,
    private val listener: OnShopItemInteractionListener? = null
) :
    RecyclerView.Adapter<TokoAdapter.TokoViewHolder>(){

    private val itemCounts = mutableMapOf<Int, Int>() // To store the count for each item
    val itemCountsRv = itemCounts
    class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.img_shopAsset)
        val priceTextView: TextView = itemView.findViewById(R.id.tv_shopPrice)
        val gemsImageView: ImageView = itemView.findViewById(R.id.img_shopGems)
        val buyButton: Button = itemView.findViewById(R.id.btn_shop)
        val quantityLayout: LinearLayout = itemView.findViewById(R.id.layout_quantity)
        val decrementButton: Button = itemView.findViewById(R.id.btn_decrement)
        val incrementButton: Button = itemView.findViewById(R.id.btn_increment)
        val itemCountTextView: TextView = itemView.findViewById(R.id.tv_itemCount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return TokoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        val currentToko = tokoList[position]
        Glide.with(holder.itemView.context)
            .load(currentToko.img ?: "") // fallback to empty URL if null
            .apply(RequestOptions().override(85, 85).fitCenter()) // Or .centerCrop()
            .into(holder.iconImageView)

        holder.priceTextView.text = currentToko.diamond.toString()

        if (itemCounts.containsKey(position)) {
            // Item has been "bought" (button clicked)
            holder.buyButton.visibility = View.GONE
            holder.quantityLayout.visibility = View.VISIBLE
            holder.priceTextView.visibility = View.GONE
            holder.gemsImageView.visibility = View.GONE
            holder.itemCountTextView.text = itemCounts[position].toString()
        } else {
            // Initial state
            holder.buyButton.visibility = View.VISIBLE
            holder.quantityLayout.visibility = View.GONE
            holder.priceTextView.visibility = View.VISIBLE
            holder.gemsImageView.visibility = View.VISIBLE
            holder.buyButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white)) // Set text color to white
            holder.buyButton.setOnClickListener {
                itemCounts[position] = 1
                notifyItemChanged(position) // Rebind to show +/-
                listener?.onItemBought(currentToko)
                // Handle the initial "buy" action (e.g., check gems)
            }
        }

        holder.decrementButton.setOnClickListener {
            val currentCount = itemCounts[position] ?: 1
            if (currentCount > 1) {
                itemCounts[position] = currentCount - 1
                holder.itemCountTextView.text = (currentCount - 1).toString()
                listener?.onItemCountChanged(currentToko, currentCount - 1) // Notify the fragment
            } else {
                // Revert to the "Beli" button
                itemCounts.remove(position)
                notifyItemChanged(position) // Rebind to show "Beli"
                listener?.onItemCountChanged(currentToko, 0) // Notify the fragment
            }
            // Handle decrement logic (e.g., updating cart)
        }

        holder.incrementButton.setOnClickListener {
            val currentCount = itemCounts[position] ?: 0
            val maxCount = currentToko.jumlah ?: Int.MAX_VALUE // fallback in case jumlah is null

            if (currentCount < maxCount) {
                itemCounts[position] = currentCount + 1
                holder.itemCountTextView.text = (currentCount + 1).toString()
                listener?.onItemCountChanged(currentToko, currentCount + 1)
            } else {
                // Optional: You can show a toast or visual feedback
                Toast.makeText(
                    holder.itemView.context,
                    "Maksimal jumlah pembelian adalah $maxCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount() = tokoList.size

}