package com.kiero.data.parent.plan.di

import com.kiero.domain.repository.parent.plan.PlanRepository
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