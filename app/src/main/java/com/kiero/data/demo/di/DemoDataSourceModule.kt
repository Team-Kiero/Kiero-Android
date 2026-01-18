package com.kiero.data.demo.di

import com.kiero.data.demo.remote.datasource.DemoDataSource
import com.kiero.data.demo.remote.datasourceimpl.DemoDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DemoDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindDemoDataSource(
        demoDataSourceImpl: DemoDataSourceImpl,
    ): DemoDataSource
}