package com.kiero.core.security.di

import android.content.Context
import com.kiero.core.security.CryptoManager
import com.kiero.core.security.TinkCryptoManager
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
    fun providesCryptoManager(
        @ApplicationContext context: Context
    ): CryptoManager {
        return TinkCryptoManager(context)
    }
}