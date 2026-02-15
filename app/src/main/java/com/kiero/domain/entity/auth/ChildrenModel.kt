package com.kiero.domain.entity.auth

import com.kiero.data.auth.remote.dto.response.ChildrenResponseDto

data class ChildrenModel(
    val childId: Long,
    val childLastName: String,
    val childFirstName: String
)

fun ChildrenResponseDto.toModel() = ChildrenModel(
    childId = childId,
    childLastName = childLastName,
    childFirstName = childFirstName
)
