package com.kiero.data.kid.schedule.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleImageUploadResponseDto (
    @SerialName("presignedUrl")
    val presignedUrl: String,
    @SerialName("fileName")
    val fileName: String
)