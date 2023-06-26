package com.andresuryana.dicodingstories.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

object FileHelper {

    fun createImageTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(UUID.randomUUID().toString(), ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val file = createImageTempFile(context)

        val input = contentResolver.openInputStream(selectedImg) as InputStream
        val output: OutputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int
        while (input.read(buffer).also { length = it } > 0) output.write(buffer, 0, length)
        output.close()
        input.close()

        return file
    }
}