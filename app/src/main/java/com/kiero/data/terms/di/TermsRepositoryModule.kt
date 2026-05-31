package com.kiero.data.terms.di

import com.kiero.data.terms.repository.TermsRepository
import com.kiero.data.terms.repositoryimpl.TermsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TermsRepositoryModule {
    @Binds
    @Singleton
    fun bindTermsRepository(
        termsRepositoryImpl: TermsRepositoryImpl
    ): TermsRepository
}