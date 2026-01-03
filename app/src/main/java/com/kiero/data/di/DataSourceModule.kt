package com.kiero.data.di

import com.kiero.data.auth.remote.datasource.DummyDataSource
import com.kiero.data.auth.remote.datasourceimpl.DummyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DummyDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindDummyDataSource(
        dummyDataSourceImpl: DummyDataSourceImpl,
    ): DummyDataSource
}