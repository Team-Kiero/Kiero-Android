package com.kiero.data.parent.plan.di

import com.kiero.data.parent.plan.remote.datasource.PlanDataSource
import com.kiero.data.parent.plan.remote.datasourceimpl.PlanDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParentDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindPlanDataSource(
        planDataSourceImpl: PlanDataSourceImpl
    ): PlanDataSource
}