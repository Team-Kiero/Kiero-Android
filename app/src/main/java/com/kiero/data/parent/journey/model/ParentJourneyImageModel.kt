package com.kiero.data.parent.journey.model

import com.kiero.data.parent.journey.remote.dto.response.ParentJourneyImageResponseDto

data class ParentJourneyImageModel(
    val imageUrl: String
)

fun ParentJourneyImageResponseDto.toModel() = ParentJourneyImageModel(
    imageUrl = imageUrl
)
