package com.kiero.data.terms.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.terms.remote.api.TermsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TermsServiceModule {
    @Provides
    @Singleton
    fun provideTermsService(
        @AuthNetwork retrofit: Retrofit,
    ): TermsService = retrofit.create()
}