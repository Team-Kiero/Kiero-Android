package com.kiero.data.schedule.di

import com.kiero.data.schedule.datasource.ScheduleDataSource
import com.kiero.data.schedule.datasourceimpl.ScheduleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindScheduleDataSource(
        scheduleDataSourceImpl: ScheduleDataSourceImpl
    ): ScheduleDataSource
}