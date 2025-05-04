package com.fhanafi.cerdikia.ui.question

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.fhanafi.cerdikia.R
import com.fhanafi.cerdikia.helper.stripHtmlTags

class AnswerOptionAdapter(
    private var answerOptions: List<Pair<String, String>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AnswerOptionAdapter.AnswerOptionViewHolder>()  {

    private var isClickEnabled = true

    @Suppress("DEPRECATION")
    inner class AnswerOptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerButton: Button = itemView.findViewById(R.id.buttonAnswerOption)

        init {
            answerButton.setOnClickListener {
                if(isClickEnabled){
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        onItemClick(answerOptions[position].first) // -> kirim key ("a", "b", dst)
                        disableClicks()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerOptionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_answer_option, parent, false)
        return AnswerOptionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AnswerOptionViewHolder, position: Int) {
        val currentAnswer = stripHtmlTags(answerOptions[position].second)
        holder.answerButton.text = currentAnswer
    }

    override fun getItemCount() = answerOptions.size

    fun updateOptions(newOptions: List<Pair<String, String>>) {
        answerOptions = newOptions
        notifyDataSetChanged()
    }

    fun disableClicks() {
        isClickEnabled = false
        notifyDataSetChanged() // Rebind to update button states
    }

    fun enableClicks() {
        isClickEnabled = true
        notifyDataSetChanged() // Rebind to update button states
    }
}