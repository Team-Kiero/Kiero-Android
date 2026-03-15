package com.kiero.data.kid.schedule.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.schedule.local.datasource.ImageLocalDataSource
import com.kiero.data.kid.schedule.remote.datasource.ImageUploadDataSource
import com.kiero.data.kid.schedule.repository.ImageUploadRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import javax.inject.Inject

class ImageUploadRepositoryImpl @Inject constructor(
    private val remoteDataSource: ImageUploadDataSource,
    private val localDataSource: ImageLocalDataSource
) : ImageUploadRepository {

    override suspend fun uploadImage(
        uriString: String,
        fileName: String,
        contentType: String
    ): Result<String> = suspendRunCatching {
        val optimizedFile = localDataSource.getOptimizedFile(uriString)
        val requestBody = optimizedFile.asRequestBody(contentType.toMediaTypeOrNull())

        val presignedResponse = remoteDataSource.postPresignedUrl(fileName, contentType).data
            ?: error("Presigned URL 발급 실패")

        val response = remoteDataSource.uploadImageToS3(presignedResponse.presignedUrl, requestBody)
        if (!response.isSuccessful) {
            error("S3 업로드 실패: ${response.code()} - ${response.errorBody()?.string()}")
        }

        localDataSource.deleteOriginalUri(uriString)
        optimizedFile.delete()

        Timber.d("S3 업로드 성공: ${presignedResponse.fileName}")
        presignedResponse.presignedUrl.substringBefore("?")
    }
}