package com.kiero.data.auth.remote.datasource

import com.kiero.core.network.model.DummyBaseResponse
import com.kiero.data.auth.remote.dto.response.DummyResponseDto

interface DummyDataSource {
    suspend fun getDummyList(): DummyBaseResponse<List<DummyResponseDto>>
}