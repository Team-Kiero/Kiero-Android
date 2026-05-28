package com.kiero.core.model.fcm

data class PushData(
    val type: String,
    val targetId: String? = null
)