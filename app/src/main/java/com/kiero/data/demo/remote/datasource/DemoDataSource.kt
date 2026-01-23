package com.kiero.data.demo.remote.datasource

import retrofit2.Response

interface DemoDataSource {
    suspend fun deleteDemo(): Response<Unit>

    suspend fun postDemo(): Response<Unit>
}