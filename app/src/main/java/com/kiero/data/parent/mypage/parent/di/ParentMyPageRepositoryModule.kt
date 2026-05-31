package com.kiero.data.parent.mypage.parent.di

import com.kiero.data.parent.mypage.parent.repository.ParentMyPageRepository
import com.kiero.data.parent.mypage.parent.repositoryimpl.ParentMyPageRepositoryImpl
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