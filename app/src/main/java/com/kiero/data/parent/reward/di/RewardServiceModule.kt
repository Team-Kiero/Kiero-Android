package com.kiero.data.parent.reward.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.reward.remote.api.RewardService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RewardServiceModule {

    @Provides
    @Singleton
    fun provideRewardService(
        @AuthNetwork retrofit: Retrofit,
    ): RewardService = retrofit.create()
}