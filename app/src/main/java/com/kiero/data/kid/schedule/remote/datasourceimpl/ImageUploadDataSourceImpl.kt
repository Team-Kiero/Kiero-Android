package com.kiero.data.kid.schedule.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.kid.schedule.remote.api.S3Service
import com.kiero.data.kid.schedule.remote.api.ScheduleService
import com.kiero.data.kid.schedule.remote.datasource.ImageUploadDataSource
import com.kiero.data.kid.schedule.remote.dto.request.ScheduleImageUploadRequestDto
import com.kiero.data.kid.schedule.remote.dto.response.ScheduleImageUploadResponseDto
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ImageUploadDataSourceImpl @Inject constructor(
    private val scheduleService: ScheduleService,
    private val s3Service: S3Service
) : ImageUploadDataSource {
    override suspend fun postPresignedUrl(
        fileName: String,
        contentType: String
    ): BaseResponse<ScheduleImageUploadResponseDto> =
        scheduleService.postPresignedUrl(
            request = ScheduleImageUploadRequestDto(
                fileName = fileName,
                contentType = contentType
            )
        )

    override suspend fun uploadImageToS3(
        presignedUrl: String,
        image: RequestBody
    ): Response<Unit> =
        s3Service.uploadImageToS3(
            url = presignedUrl,
            image = image
        )
}