package com.kiero.data.kid.mypage.di

import com.kiero.data.kid.mypage.repository.KidMyPageRepository
import com.kiero.data.kid.mypage.repositoryimpl.KidMyPageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface KidMyPageRepositoryModule {

    @Binds
    @Singleton
    fun bindKidMyPageRepository(
        kidMyPageRepositoryImpl: KidMyPageRepositoryImpl
    ): KidMyPageRepository
}