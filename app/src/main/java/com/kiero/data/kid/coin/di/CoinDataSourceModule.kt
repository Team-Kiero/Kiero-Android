package com.kiero.data.kid.coin.di

import com.kiero.data.kid.coin.remote.datasource.CoinDataSource
import com.kiero.data.kid.coin.remote.datasourceimpl.CoinDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class CoinDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindCoinDataSource(
        coinDataSourceImpl: CoinDataSourceImpl
    ): CoinDataSource
}