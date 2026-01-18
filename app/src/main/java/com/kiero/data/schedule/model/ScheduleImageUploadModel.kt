package com.kiero.data.schedule.model

import com.kiero.data.schedule.dto.response.ScheduleImageUploadResponseDto

data class ScheduleImageUploadModel(
    val presignedUrl: String,
    val fileName: String
)

fun ScheduleImageUploadResponseDto.toModel() = ScheduleImageUploadModel(
    presignedUrl = this.presignedUrl,
    fileName = this.fileName
)