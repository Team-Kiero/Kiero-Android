package com.kiero.data.auth.model

import com.kiero.data.auth.remote.dto.response.ChildrenResponseDto

data class ChildrenModel(
    val connectionId: Long,
    val childId: Long,
    val childLastName: String,
    val childFirstName: String
)

fun ChildrenResponseDto.toModel() = ChildrenModel(
    connectionId = id,
    childId = childId,
    childLastName = childLastName,
    childFirstName = childFirstName
)
