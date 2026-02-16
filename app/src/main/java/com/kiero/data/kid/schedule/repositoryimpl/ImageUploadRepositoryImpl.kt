package com.kiero.data.kid.schedule.repositoryimpl

import android.content.Context
import androidx.core.net.toUri
import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.schedule.remote.datasource.ImageUploadDataSource
import com.kiero.data.kid.schedule.repository.ImageUploadRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    private val dataSource: ImageUploadDataSource,
    @param:ApplicationContext private val context: Context
) : ImageUploadRepository {
    override suspend fun uploadImage(
        uriString: String,
        fileName: String,
        contentType: String
    ): Result<String> = suspendRunCatching {
        val presignedResponse = dataSource.postPresignedUrl(
            fileName = fileName,
            contentType = contentType
        ).data ?: error("Presigned URL 발급 실패")

        val presignedUrl = presignedResponse.presignedUrl

        val uri = uriString.toUri()
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: error("InputStream을 열 수 없습니다. URI: $uri")

        val byteArray = inputStream.use { it.readBytes() }

        val requestBody = byteArray.toRequestBody(
            contentType.toMediaTypeOrNull(),
            0,
            byteArray.size
        )

        val response = dataSource.uploadImageToS3(
            presignedUrl = presignedUrl,
            image = requestBody
        )

        if (!response.isSuccessful) {
            Timber.d("S3 업로드 실패: ${response.code()} - ${response.errorBody()?.string()}")
        }

        Timber.d("S3 업로드 성공: ${presignedResponse.fileName}")

        presignedUrl.substringBefore("?")
    }
}