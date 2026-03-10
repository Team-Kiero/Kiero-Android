package com.kiero.data.parent.journey.di

import com.kiero.data.parent.journey.remote.datasource.ParentJourneyDataSource
import com.kiero.data.parent.journey.remote.datasourceimpl.ParentJourneyDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParentJourneyDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindParentJourneyDataSource(
        parentJourneyDataSourceImpl: ParentJourneyDataSourceImpl
    ): ParentJourneyDataSource
}