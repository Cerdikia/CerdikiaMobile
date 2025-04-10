package com.fhanafi.cerdikia.ui.stage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R

class MateriAdapter(private var materiList: List<MateriItem>) :
    RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {

    class MateriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text_materi_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.text_materi_description)
        val iconImageView: ImageView = itemView.findViewById(R.id.icon_materi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi, parent, false)
        return MateriViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val currentItem = materiList[position]
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.iconImageView.setImageResource(currentItem.iconResourceId)

    }

    override fun getItemCount() = materiList.size

    fun setData(newList: List<MateriItem>) {
        materiList = newList
        notifyDataSetChanged()
    }
}