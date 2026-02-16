package com.kiero.core.common.util

import android.content.Context
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ImageUriManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun createTempImageUri(): String? {
        return try {
            val directory = File(context.cacheDir, "images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "${System.currentTimeMillis()}.jpg")
            file.createNewFile()

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            ).toString()
        } catch (e: IOException) {
            Timber.e(e, "파일 생성 실패")
            null
        }
    }
}