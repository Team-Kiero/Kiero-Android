package com.kiero.data.parent.signup.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.signup.remote.api.ParentSignUpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParentSignUpServiceModule {
    @Provides
    @Singleton
    fun provideParentSignUpService(
        @AuthNetwork retrofit: Retrofit
    ): ParentSignUpService = retrofit.create()
}
