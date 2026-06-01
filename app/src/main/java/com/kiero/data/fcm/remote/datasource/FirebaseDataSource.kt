package com.kiero.data.fcm.remote.datasource

interface FirebaseDataSource {
    suspend fun getFcmToken(): String?
}