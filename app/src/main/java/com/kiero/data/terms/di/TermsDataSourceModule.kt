package com.kiero.data.terms.di

import com.kiero.data.terms.remote.datasource.TermsDataSource
import com.kiero.data.terms.remote.datasourceimpl.TermsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TermsDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindTermsDataSource(
        termsDataSourceImpl: TermsDataSourceImpl
    ): TermsDataSource
}