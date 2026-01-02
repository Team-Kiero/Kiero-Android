package com.Kiero.data.auth.repositoryimpl

import com.Kiero.core.common.util.suspendRunCatching
import com.Kiero.data.auth.mapper.DummyMapper
import com.Kiero.data.auth.remote.datasource.DummyDataSource
import com.Kiero.domain.auth.model.DummyEntity
import com.Kiero.domain.auth.repository.DummyRepository
import javax.inject.Inject


class DummyRepositoryImpl @Inject constructor(
    private val dummyDataSource: DummyDataSource,
    private val mapper: DummyMapper,
) : DummyRepository {
    override suspend fun getDummyList(): Result<List<DummyEntity>> = suspendRunCatching {
        dummyDataSource.getDummyList().data.map { mapper.mapDtoToEntity(it) }
    }
}

