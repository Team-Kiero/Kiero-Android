package com.kiero.data.kid.schedule.repository

import android.content.Context
import androidx.core.net.toUri
import com.kiero.data.kid.schedule.remote.api.S3Service
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

class PresignedUrlRepository @Inject constructor(
    private val s3Service: S3Service,
    @param:ApplicationContext private val context: Context
) {
    suspend fun uploadImage(presignedUrl: String, uriString: String): Boolean {
        try {
            val uri = uriString.toUri()
            Timber.e("uploadimage $uri")

            val inputStream = context.contentResolver.openInputStream(uri)
                ?: run {
                    Timber.e("InputStream을 열 수 없습니다. URI: $uri")
                    return false
                }

            val byteArray = inputStream.readBytes()
            inputStream.close()

            val requestBody = byteArray.toRequestBody(
                "image/jpeg".toMediaTypeOrNull(),
                0,
                byteArray.size
            )

            val response = s3Service.uploadImageToS3(presignedUrl, requestBody)

            if (response.isSuccessful) {
                Timber.d("S3 업로드 성공!")
                return true
            } else {
                Timber.d("S3 업로드 실패: ${response.code()} - ${response.errorBody()?.string()}")
                return false
            }

        } catch (e: Exception) {
            Timber.e(e, "S3 업로드 중 예외 발생")
            return false
        }
    }
}