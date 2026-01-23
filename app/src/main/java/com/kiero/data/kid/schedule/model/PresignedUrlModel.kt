package com.kiero.data.kid.schedule.model

import com.kiero.data.kid.schedule.remote.dto.response.PresignedUrlResponse

data class PresignedUrlModel(
    val presignedUrl: String,
    val fileName: String
)

fun PresignedUrlResponse.toModel() = PresignedUrlModel(
    presignedUrl = this.data.presignedUrl,
    fileName = this.data.fileName
)
