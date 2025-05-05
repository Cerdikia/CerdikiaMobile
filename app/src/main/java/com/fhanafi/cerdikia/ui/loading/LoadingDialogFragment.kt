package com.fhanafi.cerdikia.ui.loading

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fhanafi.cerdikia.R

class LoadingDialogFragment : DialogFragment() {

    private val descriptions = listOf(
        "Setiap detik adalah kesempatan belajar dan berkembang! Siap naik ke level berikutnya?",
        "Belajar hari ini, bintang masa depan esok hari!",
        "Kumpulkan XP, raih pencapaian, jadilah juara di kelasmu!",
        "Ilmu adalah kekuatan—ayo isi harimu dengan tantangan baru!",
        "Semangatmu hari ini menentukan prestasimu esok!",
        "Satu langkah kecil hari ini bisa jadi lompatan besar besok!",
        "Ayo uji kemampuanmu dan menangkan lebih banyak gems!",
        "Belajar nggak harus membosankan—siap main dan belajar bareng kami?",
        "Tingkatkan levelmu dan buktikan kamu bisa jadi yang terbaik!",
        "Setiap jawaban benar adalah langkah menuju kesuksesan!",
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.layout_loading_overlay, container, false)
        val descriptionTextView = view.findViewById<TextView>(R.id.tv_loading_description)
        descriptionTextView.text = descriptions.random()
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog?.setCancelable(false) // Prevent closing
    }
}
