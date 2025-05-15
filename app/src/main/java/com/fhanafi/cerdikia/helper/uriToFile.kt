package com.fhanafi.cerdikia.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)

    // Decode the original image
    val originalBitmap = BitmapFactory.decodeStream(inputStream)
    inputStream?.close()

    // Create compressed file in cache directory
    val compressedFile = File.createTempFile("compressed_image", ".jpg", context.cacheDir)

    // Compress the bitmap to reduce file size (adjust quality as needed)
    val outputStream = FileOutputStream(compressedFile)
    originalBitmap?.compress(Bitmap.CompressFormat.JPEG, 70, outputStream) // 70% quality
    outputStream.flush()
    outputStream.close()

    return compressedFile
}
