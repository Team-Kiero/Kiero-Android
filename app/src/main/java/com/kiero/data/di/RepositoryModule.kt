package com.kiero.data.di

import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.auth.repositoryimpl.DummyRepositoryImpl
import com.kiero.data.auth.repository.DummyRepository
import com.kiero.data.auth.repositoryimpl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsDummyRepository(
        dummyRepositoryImpl: DummyRepositoryImpl
    ): DummyRepository

    @Binds
    @Singleton
    fun bindsAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

}