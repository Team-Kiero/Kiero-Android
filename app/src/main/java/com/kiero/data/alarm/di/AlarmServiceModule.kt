package com.kiero.data.alarm.di

import com.kiero.data.alarm.api.AlarmService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmServiceModule {
    @Provides
    @Singleton
    fun providesAlarmService(retrofit: Retrofit): AlarmService =
        retrofit.create()
}