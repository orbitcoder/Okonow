package com.noitacilppa.okonow.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {
    /**
     * Copies an image from a URI to the app's internal storage.
     * Returns the absolute path of the saved file, or null if it fails.
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_image_${UUID.randomUUID()}.jpg"
            val directory = File(context.filesDir, "profile_pictures")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            
            // Delete old profile pictures to save space
            directory.listFiles()?.forEach { it.delete() }
            
            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)
            
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
