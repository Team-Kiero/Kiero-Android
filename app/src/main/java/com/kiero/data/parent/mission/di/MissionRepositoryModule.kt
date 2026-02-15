package com.kiero.data.parent.mission.di

import com.kiero.data.parent.mission.repositoryimpl.AutoMissionRepositoryImpl
import com.kiero.data.parent.mission.repositoryimpl.MissionRepositoryImpl
import com.kiero.domain.repository.parent.mission.AutoMissionRepository
import com.kiero.domain.repository.parent.mission.MissionRepository
import com.kiero.domain.repository.parent.mission.ParentMissionAddRepository
import com.kiero.data.parent.mission.repositoryimpl.ParentMissionAddRepositoryImpl
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