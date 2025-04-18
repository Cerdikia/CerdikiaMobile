package com.fhanafi.cerdikia.ui.shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R

class MisiHarianAdapter(private val misiList: List<MisiHarian>) :
    RecyclerView.Adapter<MisiHarianAdapter.MisiHarianViewHolder>() {

    class MisiHarianViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.img_quest)
        val titleTextView: TextView = itemView.findViewById(R.id.tv_shopQuest)
        val progressTextView: TextView = itemView.findViewById(R.id.tv_progress)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressXP)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MisiHarianViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_misi_harian, parent, false)
        return MisiHarianViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MisiHarianViewHolder, position: Int) {
        val currentMisi = misiList[position]
        holder.iconImageView.setImageResource(currentMisi.iconResourceId)
        holder.titleTextView.text = currentMisi.title
        holder.progressTextView.text = "${currentMisi.progress} / ${currentMisi.maxProgress}"

    }

    override fun getItemCount() = misiList.size
}


data class MisiHarian(
    val iconResourceId: Int,
    val title: String,
    val progress: Int,
    val maxProgress: Int
)
