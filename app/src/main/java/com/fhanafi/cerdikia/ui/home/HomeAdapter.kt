package com.fhanafi.cerdikia.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R


class HomeAdapter(private val itemList: List<HomePlaceHolderItem>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.text_title)
            val progressTextView: TextView = itemView.findViewById(R.id.text_progress)
            val descriptionTextView: TextView = itemView.findViewById(R.id.text_description)
            val mulaiButton: Button = itemView.findViewById(R.id.button_mulai)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_placeholder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.titleTextView.text = currentItem.title
        holder.progressTextView.text = currentItem.progress
        holder.descriptionTextView.text = currentItem.description
        // Set OnClickListener for the button if needed
        holder.mulaiButton.setOnClickListener {
            // Handle button click
        }
    }

    override fun getItemCount() = itemList.size
}