package com.kiero.data.kid.schedule.di

import com.kiero.data.kid.schedule.remote.datasource.ScheduleDataSource
import com.kiero.data.kid.schedule.remote.datasourceimpl.ScheduleDataSourceImpl
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