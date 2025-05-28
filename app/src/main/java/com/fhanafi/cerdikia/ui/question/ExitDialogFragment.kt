package com.fhanafi.cerdikia.ui.question

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fhanafi.cerdikia.R

class ExitDialogFragment(private val onExitConfirmed: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.popup_stage, null)

        val btnOke = view.findViewById<Button>(R.id.btn_oke)
        val btnClose = view.findViewById<TextView>(R.id.textViewClose)

        btnOke.setOnClickListener {
            onExitConfirmed()
            dismiss()
        }

        btnClose.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        return builder.create()
    }

    // Optional: make background transparent and corner radius respected
    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
