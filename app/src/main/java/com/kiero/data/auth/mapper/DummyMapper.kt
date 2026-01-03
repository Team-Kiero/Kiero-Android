package com.kiero.data.auth.mapper

import com.kiero.data.auth.remote.dto.response.DummyResponseDto
import com.kiero.data.auth.model.DummyEntity
import javax.inject.Inject

class DummyMapper @Inject constructor() {
    fun mapDtoToEntity(dto : DummyResponseDto) : DummyEntity {
        return DummyEntity(
            profile = dto.avatar,
            firstName = dto.firstName,
            id = dto.id,
            lastName = dto.lastName,
        )
    }

    fun mapEntityToDto(entity : DummyEntity) : DummyResponseDto {
        return DummyResponseDto(
            avatar = entity.profile,
            firstName = entity.firstName,
            id = entity.id,
            lastName = entity.lastName,
            email = ""
        )
    }
}


