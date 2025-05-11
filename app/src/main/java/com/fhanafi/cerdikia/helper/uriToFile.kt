package com.fhanafi.cerdikia.helper

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)

    val inputStream = contentResolver.openInputStream(uri)
    val outputStream = FileOutputStream(tempFile)

    inputStream?.copyTo(outputStream)

    inputStream?.close()
    outputStream.close()

    return tempFile
}