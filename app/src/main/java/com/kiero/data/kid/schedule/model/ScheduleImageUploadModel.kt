package com.kiero.data.kid.schedule.model

import com.kiero.data.kid.schedule.remote.dto.response.ScheduleImageUploadResponseDto

data class ScheduleImageUploadModel(
    val presignedUrl: String,
    val fileName: String
)

fun ScheduleImageUploadResponseDto.toModel() = ScheduleImageUploadModel(
    presignedUrl = this.presignedUrl,
    fileName = this.fileName
)