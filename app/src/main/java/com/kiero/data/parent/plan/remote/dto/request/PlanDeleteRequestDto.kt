package com.kiero.data.parent.plan.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanDeleteRequestDto(
    @SerialName("isIncludeFollowing")
    val isIncludeFollowing: Boolean? = null,
)
