package com.fhanafi.cerdikia.helper

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

object PdfUtils {

    fun savePdfToStorage(context: Context, body: ResponseBody) {
        val fileName = "Bukti_penukaran_${System.currentTimeMillis()}.pdf"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10+ (API 29+)
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val resolver = context.contentResolver
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val uri = resolver.insert(collection, contentValues)

            uri?.let {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    body.byteStream().copyTo(outputStream)
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, contentValues, null, null)

                Toast.makeText(context, "Disimpan di: Unduhan", Toast.LENGTH_LONG).show()
                openPdfFile(context, uri)
            } ?: run {
                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_LONG).show()
            }

        } else {
            // Legacy method for Android 9 and below
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            try {
                val inputStream = body.byteStream()
                val outputStream = FileOutputStream(file)
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                Toast.makeText(context, "Disimpan di: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                openPdfFile(context, file)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Gagal menyimpan file", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openPdfFile(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        openPdfWithIntent(context, uri)
    }

    private fun openPdfFile(context: Context, uri: Uri) {
        openPdfWithIntent(context, uri)
    }

    private fun openPdfWithIntent(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_LONG).show()
        }
    }
}
