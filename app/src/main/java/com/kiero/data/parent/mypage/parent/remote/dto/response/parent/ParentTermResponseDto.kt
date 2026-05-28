package com.kiero.data.parent.mypage.parent.remote.dto.response.parent

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParentTermResponseDto(
    @SerialName("linkType")
    val linkType: String,
    @SerialName("link")
    val link: String
)