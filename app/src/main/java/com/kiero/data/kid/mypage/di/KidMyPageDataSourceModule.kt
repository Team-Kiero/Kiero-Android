package com.kiero.data.kid.mypage.di

import com.kiero.data.kid.mypage.remote.datasource.KidMyPageDataSource
import com.kiero.data.kid.mypage.remote.datasourceimpl.KidMyPageDataSourceImpl
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