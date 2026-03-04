package com.kiero.core.common.app.di

import com.kiero.core.common.app.AppRestarter
import com.kiero.core.common.app.AppRestarterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindAppRestarter(impl: AppRestarterImpl): AppRestarter
}
