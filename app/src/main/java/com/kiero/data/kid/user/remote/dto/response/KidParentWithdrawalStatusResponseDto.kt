package com.kiero.data.kid.user.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KidParentWithdrawalStatusResponseDto(
    @SerialName("isParentWithdrawn")
    val isParentWithdrawn: Boolean,
)