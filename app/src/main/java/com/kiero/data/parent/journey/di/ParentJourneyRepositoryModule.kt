package com.kiero.data.parent.journey.di

import com.kiero.data.parent.journey.repository.ParentJourneyRepository
import com.kiero.data.parent.journey.repositoryimpl.ParentJourneyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ParentJourneyRepositoryModule {

    @Binds
    @Singleton
    fun bindsParentJourneyRepository(
        impl: ParentJourneyRepositoryImpl
    ): ParentJourneyRepository
}