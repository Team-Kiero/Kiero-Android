package com.kiero.data.kid.schedule.di

import com.kiero.data.kid.schedule.repository.ImageUploadRepository
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import com.kiero.data.kid.schedule.repositoryimpl.ImageUploadRepositoryImpl
import com.kiero.data.kid.schedule.repositoryimpl.ScheduleRepositoryImpl
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

    @Binds
    @Singleton
    fun bindImageUploadRepository(
        imageUploadRepositoryImpl: ImageUploadRepositoryImpl
    ) : ImageUploadRepository
}