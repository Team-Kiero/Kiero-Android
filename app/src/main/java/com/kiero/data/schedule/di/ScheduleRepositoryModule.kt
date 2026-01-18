package com.kiero.data.schedule.di

import com.kiero.data.schedule.repository.ScheduleRepository
import com.kiero.data.schedule.repositoryimpl.ScheduleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ScheduleRepositoryModule {
    @Binds
    @Singleton
    fun bindsScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ) : ScheduleRepository
}