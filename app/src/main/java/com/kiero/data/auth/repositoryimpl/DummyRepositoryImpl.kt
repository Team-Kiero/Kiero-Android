package com.kiero.data.auth.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.auth.mapper.DummyMapper
import com.kiero.data.auth.remote.datasource.DummyDataSource
import com.kiero.data.auth.model.DummyEntity
import com.kiero.data.auth.repository.DummyRepository
import javax.inject.Inject


class DummyRepositoryImpl @Inject constructor(
    private val dummyDataSource: DummyDataSource,
    private val mapper: DummyMapper,
) : DummyRepository {
    override suspend fun getDummyList(): Result<List<DummyEntity>> = suspendRunCatching {
        dummyDataSource.getDummyList().data.map { mapper.mapDtoToEntity(it) }
    }
}

