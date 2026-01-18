package com.kiero.data.auth.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.core.network.di.NoAuthNetwork
import com.kiero.data.auth.remote.api.AuthParentService
import com.kiero.data.auth.remote.api.AuthService
import com.kiero.data.auth.remote.api.ReissueService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthServiceModule {
    @Provides
    @Singleton
    fun providesAuthService(
        @NoAuthNetwork retrofit: Retrofit
    ): AuthService = retrofit.create()

    @Provides
    @Singleton
    fun provideReissueService(@NoAuthNetwork retrofit: Retrofit): ReissueService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideLogoutService(@AuthNetwork retrofit: Retrofit): AuthParentService =
        retrofit.create()
}