package com.kiero.data.auth.remote.dto.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChildrenResponseDto(
    @SerializedName("childId")
    val childId: Long,
    @SerializedName("childLastName")
    val childLastName: String,
    @SerializedName("childFirstName")
    val childFirstName: String
)
