package com.kiero.data.demo.repositoryimpl

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.demo.remote.datasource.DemoDataSource
import com.kiero.data.demo.repository.DemoRepository
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val demoDataSource: DemoDataSource
) : DemoRepository {
    override suspend fun deleteDemo(): Result<Unit> = suspendRunCatching {
        demoDataSource.deleteDemo()
    }
    override suspend fun postDemo(): Result<Unit> = suspendRunCatching {
        demoDataSource.postDemo()
    }
}