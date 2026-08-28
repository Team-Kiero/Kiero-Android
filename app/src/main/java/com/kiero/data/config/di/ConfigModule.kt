package com.kiero.data.config.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.kiero.data.config.remote.datasource.ConfigRemoteDataSource
import com.kiero.data.config.remote.datasourceimpl.RemoteConfigDataSourceImpl
import com.kiero.data.config.repository.ConfigRepository
import com.kiero.data.config.repositoryimpl.ConfigRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

    @Binds
    @Singleton
    abstract fun bindConfigRemoteDataSource(
        remoteConfigDataSourceImpl: RemoteConfigDataSourceImpl,
    ): ConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindConfigRepository(
        configRepositoryImpl: ConfigRepositoryImpl,
    ): ConfigRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig
    }
}
