package com.kiero.data.mission.di

import com.kiero.data.auth.local.datasource.AuthLocalDataSource
import com.kiero.data.auth.local.datasourceimpl.AuthLocalDataSourceImpl
import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.datasourceimpl.AuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MissionDataSourceModule {

}