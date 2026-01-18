package com.kiero.data.kid.wish.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.kid.wish.remote.api.WishService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishServiceModule {

    @Provides
    @Singleton
    fun providesWishService(
        @AuthNetwork retrofit: Retrofit
    ): WishService =
        retrofit.create()
}
