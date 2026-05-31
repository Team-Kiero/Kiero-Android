package com.kiero.data.parent.mypage.kid.di

import com.kiero.data.parent.mypage.kid.repository.KidMyPageRepository
import com.kiero.data.parent.mypage.kid.repositoryimpl.KidMyPageRepositoryImpl
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