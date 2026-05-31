package com.kiero.data.parent.mypage.parent.di

import com.kiero.data.parent.mypage.parent.remote.datasource.ParentMyPageDataSource
import com.kiero.data.parent.mypage.parent.remote.datasourceimpl.ParentMyPageDataSourceImpl
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