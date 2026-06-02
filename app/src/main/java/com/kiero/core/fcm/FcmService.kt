package com.kiero.core.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kiero.core.notification.KieroNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class KieroFcmService : FirebaseMessagingService() {
    @Inject
    lateinit var notificationManager: KieroNotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("새로운 FCM 토큰 발급: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.d("푸시 알림 수신: ${message.data}")

        val title = message.notification?.title
        val body = message.notification?.body

        if (title.isNullOrEmpty() || body.isNullOrEmpty()) {
            Timber.w("푸시 알림의 제목이나 내용이 비어있어 무시합니다. (title: $title, body: $body)")
            return
        }

        notificationManager.showPushNotification(title, body, message.data)
    }
}