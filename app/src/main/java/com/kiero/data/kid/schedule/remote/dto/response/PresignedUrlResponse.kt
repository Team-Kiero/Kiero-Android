package com.kiero.data.kid.schedule.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PresignedUrlResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PresignedUrlData
)

data class PresignedUrlData(
    @SerializedName("presignedUrl") val presignedUrl: String,
    @SerializedName("fileName") val fileName: String
)