package com.kiero.data.parent.signup.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkageKidResponseDto(
    @SerialName("isRegistered")
    val isRegistered: Boolean,
    @SerialName("childId")
    val childId: Long
)
