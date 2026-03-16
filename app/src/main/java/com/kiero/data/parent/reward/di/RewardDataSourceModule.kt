package com.kiero.data.parent.reward.di

import com.kiero.data.parent.reward.remote.datasource.RewardDataSource
import com.kiero.data.parent.reward.remote.datasourceimpl.RewardDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RewardDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRewardDataSource(
        rewardDataSourceImpl: RewardDataSourceImpl,
    ): RewardDataSource
}