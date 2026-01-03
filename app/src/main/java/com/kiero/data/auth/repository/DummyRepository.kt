package com.kiero.data.auth.repository

import com.kiero.data.auth.model.DummyEntity

interface DummyRepository {
    suspend fun getDummyList(): Result<List<DummyEntity>>
}