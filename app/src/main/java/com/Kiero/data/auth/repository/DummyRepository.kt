package com.Kiero.data.auth.repository

import com.Kiero.data.auth.model.DummyEntity

interface DummyRepository {
    suspend fun getDummyList(): Result<List<DummyEntity>>
}