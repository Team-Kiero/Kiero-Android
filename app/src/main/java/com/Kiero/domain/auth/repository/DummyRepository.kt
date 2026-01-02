package com.Kiero.domain.auth.repository

import com.Kiero.domain.auth.model.DummyEntity

interface DummyRepository {
    suspend fun getDummyList(): Result<List<DummyEntity>>
}