package com.kiero.data.parent.di

import com.kiero.data.parent.plan.repository.PlanRepository
import com.kiero.data.parent.plan.repositoryimpl.PlanRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ParentRepositoryModule {
    @Binds
    @Singleton
    fun bindPlanRepository(
        planRepositoryImpl: PlanRepositoryImpl,
    ): PlanRepository
}