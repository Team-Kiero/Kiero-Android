package com.kiero.core.localstorage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kiero.core.localstorage.TokenManager
import com.kiero.core.localstorage.TokenManagerImpl
import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.core.localstorage.info.UserInfoManagerImpl
import com.kiero.core.localstorage.onboarding.OnboardingManager
import com.kiero.core.localstorage.onboarding.OnboardingManagerImpl
import com.kiero.core.security.CryptoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "kiero_auth_datastore")

    @Provides
    @Singleton
    fun provideAuthDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.authDataStore
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        dataStore: DataStore<Preferences>,
        cryptoManager: CryptoManager
    ): TokenManager {
        return TokenManagerImpl(dataStore, cryptoManager)
    }

    @Provides
    @Singleton
    fun provideOnboardingManager(
        dataStore: DataStore<Preferences>,
    ): OnboardingManager {
        return OnboardingManagerImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUserInfoManager(
        dataStore: DataStore<Preferences>,
    ): UserInfoManager {
        return UserInfoManagerImpl(dataStore)
    }
}