package com.fhanafi.cerdikia.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R

class HomeShimmerAdapter (private val itemCount: Int) : RecyclerView.Adapter<HomeShimmerAdapter.ShimmerViewHolder>() {

    inner class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_placeholder_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) {
        // Nothing to bind, it's a placeholder
    }

    override fun getItemCount(): Int = itemCount
}