package com.kiero.data.sse.di

import com.kiero.core.network.di.SseNetwork
import com.kiero.data.sse.manager.SseManager
import com.kiero.data.sse.remote.api.SseService
import com.kiero.data.sse.remote.datasource.SseDataSource
import com.kiero.data.sse.remote.datasourceimpl.SseDataSourceImpl
import com.kiero.data.sse.repository.SseRepository
import com.kiero.data.sse.repositoryimpl.SseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SseModule {

    @Provides
    @Singleton
    fun provideSseService(
        @SseNetwork retrofit: Retrofit
    ): SseService {
        return retrofit.create(SseService::class.java)
    }

    @Provides
    @Singleton
    fun provideSseDataSource(
        sseService: SseService,
        @SseNetwork okHttpClient: OkHttpClient
    ): SseDataSource {
        return SseDataSourceImpl(sseService, okHttpClient)
    }

    @Provides
    @Singleton
    fun provideSseRepository(
        sseDataSource: SseDataSource
    ): SseRepository {
        return SseRepositoryImpl(sseDataSource)
    }

    @Provides
    @Singleton
    fun provideSseManager(
        sseRepository: SseRepository
    ): SseManager {
        return SseManager(sseRepository)
    }
}
