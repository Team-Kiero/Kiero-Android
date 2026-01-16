package com.kiero.data.coin.di

import com.kiero.data.coin.api.CoinService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoinServiceModule {
    @Provides
    @Singleton
    fun providesCoinService(retrofit: Retrofit): CoinService =
        retrofit.create()
}