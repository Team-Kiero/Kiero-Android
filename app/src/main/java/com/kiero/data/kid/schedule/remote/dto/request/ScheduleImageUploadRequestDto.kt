package com.kiero.data.kid.schedule.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleImageUploadRequestDto(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("contentType")
    val contentType: String
)