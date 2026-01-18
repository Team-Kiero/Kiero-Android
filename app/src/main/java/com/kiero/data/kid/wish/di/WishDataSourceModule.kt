package com.kiero.data.kid.wish.di

import com.kiero.data.kid.wish.remote.datasource.WishDataSource
import com.kiero.data.kid.wish.remote.datasourceimpl.WishDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WishDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsWishDataSource(
        wishDataSourceImpl: WishDataSourceImpl
    ): WishDataSource
}