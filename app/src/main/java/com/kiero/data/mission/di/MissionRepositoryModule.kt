package com.kiero.data.mission.di

import com.kiero.data.auth.repository.AuthRepository
import com.kiero.data.auth.repositoryimpl.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MissionRepositoryModule {

}