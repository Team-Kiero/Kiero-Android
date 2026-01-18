package com.kiero.data.parent.remote.dto.reqeust

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ParentMissionAddRequestDto (
    @SerialName("name")
    val name : String,
    @SerialName("reward")
    val reward : Int,
    @SerialName("dueAt")
    val dueAt : String
)