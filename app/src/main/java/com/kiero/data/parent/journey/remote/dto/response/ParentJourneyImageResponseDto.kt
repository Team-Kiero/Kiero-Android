package com.kiero.data.parent.journey.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentJourneyImageResponseDto(
    @SerialName("imageUrl")
    val imageUrl: String
)
