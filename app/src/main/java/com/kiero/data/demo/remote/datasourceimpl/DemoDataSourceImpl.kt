package com.kiero.data.demo.remote.datasourceimpl

import com.kiero.data.demo.remote.api.DemoService
import com.kiero.data.demo.remote.datasource.DemoDataSource
import retrofit2.Response
import javax.inject.Inject

class DemoDataSourceImpl @Inject constructor(
    private val demoService: DemoService
) : DemoDataSource {
    override suspend fun deleteDemo(): Response<Unit> = demoService.deleteDemo()

    override suspend fun postDemo(): Response<Unit> = demoService.postDemo()
}