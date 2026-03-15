package com.kiero.data.parent.journey.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.journey.remote.api.ParentJourneyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParentJourneyServiceModule {
    @Provides
    @Singleton
    fun providesParentJourneyService(
        @AuthNetwork retrofit: Retrofit
    ): ParentJourneyService = retrofit.create()


}