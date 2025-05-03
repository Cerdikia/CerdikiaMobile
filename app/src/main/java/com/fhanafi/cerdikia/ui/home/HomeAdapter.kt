package com.fhanafi.cerdikia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.data.database.MapelEntity


class HomeAdapter(private val itemList: List<MapelEntity>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.text_title)
            val kelasTextView: TextView = itemView.findViewById(R.id.text_kelas)
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
        holder.titleTextView.text = currentItem.nama_mapel
        holder.kelasTextView.text = "Kelas : ${currentItem.kelas}"
        holder.descriptionTextView.text = "Deskripsi mapel ini belum tersedia, gunakan placeholder."
        // Set OnClickListener for the button if needed
        holder.mulaiButton.setOnClickListener {
            val materiId = position + 1 // Example: Use the position as a simple ID. You might need a more specific ID from your data.
            val bundle = Bundle().apply {
                putInt("materiId", materiId)
            }
            holder.itemView.findNavController().navigate(R.id.action_navigation_home_to_stageFragment, bundle)
        }
    }

    override fun getItemCount() = itemList.size
}