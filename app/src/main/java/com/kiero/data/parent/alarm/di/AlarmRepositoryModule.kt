package com.kiero.data.parent.alarm.di

import com.kiero.data.parent.alarm.repositoryimpl.AlarmRepositoryImpl
import com.kiero.domain.repository.parent.alarm.AlarmRepository
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