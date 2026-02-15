package com.kiero.data.parent.mission.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.mission.remote.api.MissionService
import com.kiero.data.parent.mission.remote.api.ParentMissionService
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
    fun providesMissionService(
        @AuthNetwork retrofit: Retrofit,
    ): MissionService {
        return retrofit.create(MissionService::class.java)
    }

    @Provides
    @Singleton
    fun provideParentMissionAddService(
        @AuthNetwork retrofit: Retrofit,
    ): ParentMissionService {
        return retrofit.create()
    }
}