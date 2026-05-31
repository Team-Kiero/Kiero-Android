package com.kiero.core.common.extension

import android.content.Intent
import com.kiero.core.model.fcm.PushData
import com.kiero.core.model.fcm.PushType

fun Intent?.toPushDataOrNull(): PushData? {
    val typeString = this?.getStringExtra("type")?.takeIf { it.isNotBlank() } ?: return null
    val type = PushType.fromString(typeString)
    val targetId = this.getStringExtra("targetId")
    return PushData(type, targetId)
}