package com.kiero.data.demo.remote.datasourceimpl

import com.kiero.core.network.model.BaseResponse
import com.kiero.data.demo.remote.api.DemoService
import com.kiero.data.demo.remote.datasource.DemoDataSource
import javax.inject.Inject

class DemoDataSourceImpl @Inject constructor(
    private val demoService: DemoService
) : DemoDataSource {
    override suspend fun deleteDemo(): BaseResponse<Unit> = demoService.deleteDemo()

    override suspend fun postDemo(): BaseResponse<Unit> = demoService.postDemo()
}