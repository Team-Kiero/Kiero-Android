package com.kiero.data.alarm.di

import com.kiero.data.alarm.datasource.AlarmDataSource
import com.kiero.data.alarm.datasourceimpl.AlarmDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAlarmDataSource(
        alarmDataSourceImpl: AlarmDataSourceImpl
    ): AlarmDataSource
}