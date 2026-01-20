package com.kiero.data.sse.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SseTokenResponseDto(
    @SerialName("accessToken")
    val accessToken: String
)