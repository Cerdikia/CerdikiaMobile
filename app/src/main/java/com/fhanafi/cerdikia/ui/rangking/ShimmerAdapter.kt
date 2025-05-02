package com.fhanafi.cerdikia.ui.rangking

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R
import android.view.LayoutInflater

class ShimmerAdapter(private val itemCount: Int = 15) : RecyclerView.Adapter<ShimmerAdapter.ShimmerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shimmer_rangking, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun getItemCount() = itemCount

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {}

    class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
