package com.kiero.data.parent.mypage.di

import com.kiero.data.parent.mypage.remote.datasource.ParentMyPageDataSource
import com.kiero.data.parent.mypage.remote.datasourceimpl.ParentMyPageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParentMyPageDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindParentMyPageDataSource(
        parentMyPageDataSourceImpl: ParentMyPageDataSourceImpl
    ): ParentMyPageDataSource
}