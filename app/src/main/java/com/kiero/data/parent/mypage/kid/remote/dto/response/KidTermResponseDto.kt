package com.kiero.data.parent.mypage.kid.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KidTermResponseDto(
    @SerialName("linkType")
    val linkType: String,
    @SerialName("link")
    val link: String
)