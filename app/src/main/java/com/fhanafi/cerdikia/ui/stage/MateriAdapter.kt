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

    class MateriViewHolder(itemView: View, adapter: MateriAdapter) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.icon_materi)
        private val materiAdapter = adapter
        init {
            iconImageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val materiId = materiAdapter.materiList[position].id
                    val bundle = Bundle()
                    bundle.putInt("materiId", materiId)
                    itemView.findNavController().navigate(R.id.action_stageFragment_to_soalFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi, parent, false)
        return MateriViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val currentItem = materiList[position]
        holder.iconImageView.setImageResource(currentItem.iconResourceId)

    }

    override fun getItemCount() = materiList.size

    fun setData(newList: List<MateriItem>) {
        materiList = newList
        notifyDataSetChanged()
    }
}