package com.kiero.data.mission.di

import com.kiero.data.mission.remote.api.MissionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MissionServiceModule {
    @Provides
    @Singleton
    fun providesMissionService(retrofit: Retrofit): MissionService =
        retrofit.create()
}