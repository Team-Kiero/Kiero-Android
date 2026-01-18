package com.kiero.data.schedule.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.schedule.api.ScheduleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScheduleServiceModule {
    @Provides
    @Singleton
    fun provideScheduleService(
        @AuthNetwork retrofit: Retrofit
    ): ScheduleService =
        retrofit.create()
}