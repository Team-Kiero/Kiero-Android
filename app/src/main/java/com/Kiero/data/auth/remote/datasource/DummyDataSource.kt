package com.Kiero.data.auth.remote.datasource

import com.Kiero.core.network.model.DummyBaseResponse
import com.Kiero.data.auth.remote.dto.response.DummyResponseDto

interface DummyDataSource {
    suspend fun getDummyList(): DummyBaseResponse<List<DummyResponseDto>>
}