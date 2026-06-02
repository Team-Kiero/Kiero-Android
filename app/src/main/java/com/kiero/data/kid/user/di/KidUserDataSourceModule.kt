package com.kiero.data.kid.user.di

import com.kiero.data.kid.user.remote.datasource.KidUserDataSource
import com.kiero.data.kid.user.remote.datasourceimpl.KidUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KidUserDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindKidUserDataSource(
        kidUserDataSourceImpl: KidUserDataSourceImpl
    ): KidUserDataSource
}