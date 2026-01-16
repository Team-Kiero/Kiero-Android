package com.kiero.data.demo.remote.datasource

import com.kiero.core.network.model.BaseResponse

interface DemoDataSource {
    suspend fun deleteDemo(): BaseResponse<Unit>

    suspend fun postDemo(): BaseResponse<Unit>
}