package com.kiero.data.auth.di

import com.kiero.core.network.auth.TokenRefreshService
import com.kiero.data.auth.serviceimpl.TokenRefreshServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenRefreshServiceModule {

    @Binds
    @Singleton
    abstract fun bindTokenRefreshService(tokenRefreshServiceImpl: TokenRefreshServiceImpl): TokenRefreshService
}
