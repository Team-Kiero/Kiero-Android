package com.Kiero.data.di

import com.Kiero.data.auth.remote.api.DummyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun providesDummyService(retrofit: Retrofit): DummyService =
        retrofit.create(DummyService::class.java)

}