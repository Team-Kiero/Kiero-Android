package com.kiero.data.kid.schedule.remote.datasource

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleImageUploadResponseDto
import okhttp3.RequestBody
import retrofit2.Response

interface ImageUploadDataSource {
    suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): BaseResponse<ScheduleImageUploadResponseDto>

    suspend fun uploadImageToS3(
        presignedUrl: String,
        image: RequestBody
    ): Response<Unit>
}