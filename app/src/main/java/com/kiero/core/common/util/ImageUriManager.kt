package com.kiero.core.common.util

import android.content.Context
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ImageUriManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun createTempImageUri(): String? = withContext(Dispatchers.IO) {
        try {
            val directory = File(context.cacheDir, "images").apply {
                if (!exists()) mkdirs()
            }
            val file = File.createTempFile("IMG_", ".jpg", directory)

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