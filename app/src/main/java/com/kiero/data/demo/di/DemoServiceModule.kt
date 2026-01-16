package com.kiero.data.demo.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.core.network.di.NoAuthNetwork
import com.kiero.data.demo.remote.api.DemoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DemoServiceModule {
    @Provides
    @Singleton
    fun providesDemoService(
        @AuthNetwork retrofit: Retrofit
    ): DemoService = retrofit.create()
}