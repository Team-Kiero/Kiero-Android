package com.kiero.data.demo.repository

interface DemoRepository {
    suspend fun deleteDemo(): Result<Unit>

    suspend fun postDemo(): Result<Unit>
}