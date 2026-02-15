package com.kiero.data.kid.wish.di

import com.kiero.data.kid.wish.repository.WishRepository
import com.kiero.data.kid.wish.repositoryimpl.WishRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WishRepositoryModule {
    @Binds
    @Singleton
    fun bindsWishRepository(
        wishRepositoryImpl: WishRepositoryImpl
    ): WishRepository

}