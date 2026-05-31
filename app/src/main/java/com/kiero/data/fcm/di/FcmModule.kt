package com.kiero.data.fcm.di

import com.kiero.core.network.di.AuthNetwork
import com.kiero.data.fcm.remote.api.FcmApi
import com.kiero.data.fcm.remote.datasource.FcmDataSource
import com.kiero.data.fcm.remote.datasource.FirebaseDataSource
import com.kiero.data.fcm.remote.datasourceimpl.FcmDataSourceImpl
import com.kiero.data.fcm.repository.FcmRepository
import com.kiero.data.fcm.repositoryimpl.FcmRepositoryImpl
import com.kiero.data.fcm.source.FirebaseDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FcmModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseDataSource(
        firebaseDataSourceImpl: FirebaseDataSourceImpl
    ): FirebaseDataSource

    @Binds
    @Singleton
    abstract fun bindFcmDataSource(
        fcmDataSourceImpl: FcmDataSourceImpl
    ): FcmDataSource

    @Binds
    @Singleton
    abstract fun bindFcmRepository(
        fcmRepositoryImpl: FcmRepositoryImpl
    ): FcmRepository

    companion object {
        @Provides
        @Singleton
        fun provideFcmApi(
            @AuthNetwork retrofit: Retrofit
        ): FcmApi {
            return retrofit.create(FcmApi::class.java)
        }
    }
}