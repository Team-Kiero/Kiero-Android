package com.kiero.data.kid.user.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.kid.user.remote.api.KidUserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KidUserServiceModule {
    @Provides
    @Singleton
    fun provideKidUserService(
        @AuthNetwork retrofit: Retrofit
    ): KidUserService = retrofit.create()
}