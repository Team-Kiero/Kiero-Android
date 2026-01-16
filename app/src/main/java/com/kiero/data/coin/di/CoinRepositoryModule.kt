package com.kiero.data.coin.di

import com.kiero.data.coin.repository.CoinRepository
import com.kiero.data.coin.repositoryimpl.CoinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CoinRepositoryModule {
    @Binds
    @Singleton
    fun bindsCoinRepository(
        coinRepositoryImpl: CoinRepositoryImpl
    ) : CoinRepository
}