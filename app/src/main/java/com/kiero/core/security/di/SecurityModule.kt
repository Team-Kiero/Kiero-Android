package com.kiero.core.security.di

import android.content.Context
import com.kiero.core.security.TinkEncryption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun providesTinkEncryption(
        @ApplicationContext context: Context
    ): TinkEncryption {
        return TinkEncryption(context)
    }
}