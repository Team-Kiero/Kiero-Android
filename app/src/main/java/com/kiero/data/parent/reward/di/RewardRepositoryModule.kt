package com.kiero.data.parent.reward.di

import com.kiero.data.parent.reward.repository.RewardRepository
import com.kiero.data.parent.reward.repositoryimpl.RewardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RewardRepositoryModule {

    @Binds
    @Singleton
    fun bindRewardRepository(
        rewardRepositoryImpl: RewardRepositoryImpl,
    ): RewardRepository
}