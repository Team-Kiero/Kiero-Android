package com.kiero.data.demo.di

import com.kiero.data.demo.repository.DemoRepository
import com.kiero.data.demo.repositoryimpl.DemoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DemoRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDemoRepository(
        demoRepositoryImpl: DemoRepositoryImpl,
    ): DemoRepository
}