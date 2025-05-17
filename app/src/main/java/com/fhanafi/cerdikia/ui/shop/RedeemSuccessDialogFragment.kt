package com.fhanafi.cerdikia.ui.shop

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.fhanafi.cerdikia.R

class RedeemSuccessDialogFragment : DialogFragment() {

    var onClose: (() -> Unit)? = null
    var onDownload: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_reedem, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)

        val btnVerifikasi = dialogView.findViewById<Button>(R.id.btn_verifikasi)
        val btnClose = dialogView.findViewById<TextView>(R.id.textViewClose)

        btnVerifikasi.setOnClickListener {
            onDownload?.invoke()
            dismiss()
        }

        btnClose.setOnClickListener {
            onClose?.invoke()
            dismiss()
        }

        return alertDialog
    }
}