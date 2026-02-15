package com.kiero.data.parent.plan.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.parent.plan.remote.api.PlanService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParentServiceModule {
    @Provides
    @Singleton
    fun providePlanService(
        @AuthNetwork retrofit: Retrofit,
    ): PlanService = retrofit.create()
}