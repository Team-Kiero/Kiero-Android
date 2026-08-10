package com.kiero.data.auth.remote.dto.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ChildrenResponseDto(
    @SerializedName("id")
    val id: Long, // 아이 식별자가 아닌, 부모-아이 연결 식별자
    @SerializedName("childId")
    val childId: Long,
    @SerializedName("childLastName")
    val childLastName: String,
    @SerializedName("childFirstName")
    val childFirstName: String
)
