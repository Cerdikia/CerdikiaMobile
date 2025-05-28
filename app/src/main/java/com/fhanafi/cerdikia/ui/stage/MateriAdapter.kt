package com.fhanafi.cerdikia.ui.stage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R

class MateriAdapter(
    private var materiList: List<MateriItem>,
    private val onModulClick: (View, MateriItem) -> Unit // Pass the View
) :
    RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {

    companion object {
        private const val ITEMS_PER_ROW = 8
    }

    inner class MateriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageViews = listOf<ImageView>(
            itemView.findViewById(R.id.icon_materi),
            itemView.findViewById(R.id.icon_materi2),
            itemView.findViewById(R.id.icon_materi3),
            itemView.findViewById(R.id.icon_materi4),
            itemView.findViewById(R.id.icon_materi5),
            itemView.findViewById(R.id.icon_materi6),
            itemView.findViewById(R.id.icon_materi7),
            itemView.findViewById(R.id.icon_materi8)
        )

        fun bind(startIndex: Int) {
            val unlockIndex = materiList.indexOfFirst { !it.isCompleted }

            for (i in 0 until ITEMS_PER_ROW) {
                val index = startIndex + i
                val imageView = iconImageViews[i]
                if (index < materiList.size) {
                    val materiItem = materiList[index]
                    val iconRes = when {
                        materiItem.isCompleted -> R.drawable.ic_finished
                        index == unlockIndex -> R.drawable.ic_unlock
                        else -> R.drawable.ic_lock
                    }
                    imageView.setImageResource(iconRes)
                    imageView.visibility = View.VISIBLE

                    imageView.setOnClickListener {
                        if (materiItem.isCompleted || index == unlockIndex) {
                            onModulClick(imageView, materiItem)
                        }
                    }

                } else {
                    // Hide if there are no more materi
                    imageView.setImageResource(R.drawable.ic_lock)
                    imageView.visibility = View.VISIBLE
                    imageView.setOnClickListener(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_materi, parent, false)
        return MateriViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val startIndex = position * ITEMS_PER_ROW
        holder.bind(startIndex)
    }

    override fun getItemCount(): Int {
        // Each row displays 8 items, so we need ceil(materiList.size / 8)
        return (materiList.size + ITEMS_PER_ROW - 1) / ITEMS_PER_ROW
    }

    fun setData(newList: List<MateriItem>) {
        materiList = newList
        notifyDataSetChanged()
    }
}