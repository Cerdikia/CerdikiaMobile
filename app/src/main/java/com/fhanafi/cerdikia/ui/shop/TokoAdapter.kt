package com.fhanafi.cerdikia.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R

class TokoAdapter(private val tokoList: List<Toko>) :
    RecyclerView.Adapter<TokoAdapter.TokoViewHolder>(){

    class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.img_shopAsset)
        val priceTextView: TextView = itemView.findViewById(R.id.tv_shopPrice)
        val buyButton: Button = itemView.findViewById(R.id.btn_shop)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return TokoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        val currentToko = tokoList[position]
        holder.iconImageView.setImageResource(currentToko.imgResourceId)
        holder.priceTextView.text = currentToko.price.toString()

        holder.buyButton.setOnClickListener {
            // Handle the purchase action here
            val clickedItem = tokoList[position]
            // For example:
            // Toast.makeText(holder.itemView.context, "Membeli ${clickedItem.price} Gems", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = tokoList.size

}

data class Toko(
    val imgResourceId: Int,
    val price: Int
)