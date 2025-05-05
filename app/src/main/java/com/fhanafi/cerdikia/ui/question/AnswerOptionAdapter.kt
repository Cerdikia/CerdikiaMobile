package com.fhanafi.cerdikia.ui.question

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
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
        val answerWebView: WebView = itemView.findViewById(R.id.webViewAnswerOption)

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AnswerOptionViewHolder, position: Int) {
        val answerRawHtml = answerOptions[position].second

        if (answerRawHtml.contains("<img", ignoreCase = true)) {
            // Use WebView for image-based answers
            holder.answerWebView.visibility = View.VISIBLE
            holder.answerButton.visibility = View.GONE

            val htmlContent = """
        <html>
            <head>
                <style>
                    body { font-size: 16px; padding: 0; margin: 0; text-align: center; }
                    img { max-width: 100%; height: auto; display: block; margin: auto; }
                    p { margin: 8px 0; }
                </style>
            </head>
            <body>
                $answerRawHtml
            </body>
        </html>
    """.trimIndent()

            with(holder.answerWebView) {
                settings.javaScriptEnabled = false
                settings.setSupportZoom(false)
                settings.builtInZoomControls = false
                isLongClickable = false
                isClickable = true
                isFocusable = true
                isFocusableInTouchMode = true

                loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)

                // Handle touch instead of click
                setOnTouchListener { _, _ ->
                    if (isClickEnabled) {
                        onItemClick(answerOptions[position].first)
                        disableClicks()
                    }
                    true
                }
            }
        } else {
            // Use Button for plain text answers (even with <p>)
            holder.answerWebView.visibility = View.GONE
            holder.answerButton.visibility = View.VISIBLE
            holder.answerButton.text = stripHtmlTags(answerRawHtml)
        }

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