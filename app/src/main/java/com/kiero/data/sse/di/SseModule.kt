package com.kiero.data.sse.di

import com.kiero.core.network.di.SseNetwork
import com.kiero.data.sse.remote.api.SseService
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.remote.datasourceimpl.SseDataSourceImpl
import com.kiero.domain.repository.sse.SseRepository
import com.kiero.data.sse.repositoryimpl.SseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SseModule {
    @Provides
    @Singleton
    fun provideSseService(@SseNetwork retrofit: Retrofit): SseService {
        return retrofit.create(SseService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SseBindsModule {
    @Binds
    @Singleton
    abstract fun bindSseDataSource(impl: SseDataSourceImpl): SseDataSource

    @Binds
    @Singleton
    abstract fun bindSseRepository(impl: SseRepositoryImpl): SseRepository
}