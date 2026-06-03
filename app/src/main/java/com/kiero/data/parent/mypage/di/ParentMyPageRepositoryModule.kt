package com.kiero.data.parent.mypage.di

import com.kiero.data.parent.mypage.repository.ParentMyPageRepository
import com.kiero.data.parent.mypage.repositoryimpl.ParentMyPageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ParentMyPageRepositoryModule {
    @Binds
    @Singleton
    fun bindParentMyPageRepository(
        parentMyPageRepositoryImpl: ParentMyPageRepositoryImpl
    ): ParentMyPageRepository
}