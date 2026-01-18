package com.kiero.data.mission.di

import com.kiero.data.mission.remote.datasource.MissionDataSource
import com.kiero.data.mission.remote.datasourceimpl.MissionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MissionDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMissionDataSource(
        missionDataSourceImpl: MissionDataSourceImpl
    ): MissionDataSource
}