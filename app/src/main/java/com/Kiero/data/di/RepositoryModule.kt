package com.Kiero.data.di

import com.Kiero.data.auth.repositoryimpl.DummyRepositoryImpl
import com.Kiero.domain.auth.repository.DummyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsDummyRepository(
        dummyRepositoryImpl: DummyRepositoryImpl
    ): DummyRepository

}