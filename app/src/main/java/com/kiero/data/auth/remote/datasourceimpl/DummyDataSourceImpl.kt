package com.kiero.data.auth.remote.datasourceimpl

import com.kiero.core.network.model.DummyBaseResponse
import com.kiero.data.auth.remote.api.DummyService
import com.kiero.data.auth.remote.datasource.DummyDataSource
import com.kiero.data.auth.remote.dto.response.DummyResponseDto
import javax.inject.Inject

class DummyDataSourceImpl @Inject constructor(
    private val dummyService: DummyService,
) : DummyDataSource {
    override suspend fun getDummyList(): DummyBaseResponse<List<DummyResponseDto>> =
        dummyService.getDummyLists()
}