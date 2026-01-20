package com.kiero.data.mission.di

import com.kiero.data.mission.repository.AutoMissionRepository
import com.kiero.data.mission.repository.MissionRepository
import com.kiero.data.mission.repositoryimpl.AutoMissionRepositoryImpl
import com.kiero.data.mission.repositoryimpl.MissionRepositoryImpl
import com.kiero.data.parent.repository.ParentMissionAddRepository
import com.kiero.data.parent.repositoryimpl.ParentMissionAddRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MissionRepositoryModule {

    @Binds
    @Singleton
    fun bindMissionRepository(
        missionRepositoryImpl: MissionRepositoryImpl,
    ): MissionRepository

    @Binds
    @Singleton
    fun bindParentMissionAddRepository(
        parentMissionAddRepositoryImpl: ParentMissionAddRepositoryImpl,
    ): ParentMissionAddRepository

    @Binds
    @Singleton
    fun bindAutoMissionRepository(
        autoMissionRepositoryImpl: AutoMissionRepositoryImpl
    ): AutoMissionRepository
}