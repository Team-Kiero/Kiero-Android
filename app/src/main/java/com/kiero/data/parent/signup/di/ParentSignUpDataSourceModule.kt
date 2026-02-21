package com.kiero.data.parent.signup.di

import com.kiero.data.parent.signup.remote.datasource.ParentSignUpDataSource
import com.kiero.data.parent.signup.remote.datasourceimpl.ParentSignUpDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParentSignUpDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindParentSignUpDataSource(
        parentSignUpDataSourceImpl: ParentSignUpDataSourceImpl
    ): ParentSignUpDataSource
}
