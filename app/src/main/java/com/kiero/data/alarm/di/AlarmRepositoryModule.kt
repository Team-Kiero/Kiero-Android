package com.kiero.data.alarm.di

import com.kiero.data.alarm.repository.AlarmRepository
import com.kiero.data.alarm.repositoryimpl.AlarmRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AlarmRepositoryModule {

    @Binds
    @Singleton
    fun bindsAlarmRepository(
        impl: AlarmRepositoryImpl
    ): AlarmRepository
}