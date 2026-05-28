package com.kiero.data.parent.mypage.kid.di

import com.kiero.data.parent.mypage.kid.remote.datasource.KidMyPageDataSource
import com.kiero.data.parent.mypage.kid.remote.datasourceimpl.KidMyPageDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KidMyPageDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindKidMyPageDataSource(
        kidMyPageDataSourceImpl: KidMyPageDataSourceImpl
    ): KidMyPageDataSource
}