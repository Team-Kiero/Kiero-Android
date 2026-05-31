package com.kiero.data.parent.mypage.parent.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.mypage.parent.remote.api.ParentMyPageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParentMyPageServiceModule {
    @Provides
    @Singleton
    fun provideParentMyPageService(
        @AuthNetwork retrofit: Retrofit,
    ): ParentMyPageService = retrofit.create()
}