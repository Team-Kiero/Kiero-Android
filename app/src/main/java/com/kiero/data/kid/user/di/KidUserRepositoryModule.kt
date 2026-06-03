package com.kiero.data.kid.user.di

import com.kiero.data.kid.user.repository.KidUserRepository
import com.kiero.data.kid.user.repositoryimpl.KidUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface KidUserRepositoryModule {

    @Binds
    @Singleton
    fun bindKidUserRepository(
        kidUserRepositoryImpl: KidUserRepositoryImpl
    ): KidUserRepository
}