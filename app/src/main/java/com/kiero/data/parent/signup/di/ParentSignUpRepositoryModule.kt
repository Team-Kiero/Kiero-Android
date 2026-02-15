package com.kiero.data.parent.signup.di

import com.kiero.data.parent.signup.repository.ParentSignUpRepository
import com.kiero.data.parent.signup.repositoryimpl.ParentSignUpRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParentSignUpRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindParentSignUpRepository(
        parentSignUpRepositoryImpl: ParentSignUpRepositoryImpl
    ): ParentSignUpRepository
}
