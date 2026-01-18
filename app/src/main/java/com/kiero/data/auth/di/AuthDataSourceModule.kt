package com.kiero.data.auth.di

import com.kiero.data.auth.remote.datasource.AuthDataSource
import com.kiero.data.auth.remote.datasourceimpl.AuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl,
    ): AuthDataSource
}