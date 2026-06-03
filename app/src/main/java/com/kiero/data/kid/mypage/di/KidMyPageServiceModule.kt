package com.kiero.data.kid.mypage.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.kid.mypage.remote.api.KidMyPageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KidMyPageServiceModule {
    @Provides
    @Singleton
    fun provideKidMyPageService(
        @AuthNetwork retrofit: Retrofit
    ): KidMyPageService = retrofit.create()
}