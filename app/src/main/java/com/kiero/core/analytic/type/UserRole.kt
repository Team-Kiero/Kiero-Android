package com.kiero.core.analytic.type

import com.kiero.core.analytic.UserPropertyKey

enum class UserRole(override val value: String) : UserProperty {
    PARENT("parent"),
    CHILD("child");

    override val key: String get() = UserPropertyKey.USER_ROLE
}

enum class LoginMethod(override val value: String) : UserProperty {
    KAKAO("kakao"),
    APPLE("apple"),
    INVITE_CODE("invite_code");

    override val key: String get() = UserPropertyKey.LOGIN_METHOD
}


enum class NotificationPermissionState(override val value: String) : UserProperty {
    GRANTED("granted"),
    DENIED("denied"),
    NOT_DETERMINED("not_determined");

    override val key: String get() = UserPropertyKey.NOTIFICATION_PERMISSION
}

data class PushEnabled(val enabled: Boolean) : UserProperty {
    override val key: String get() = UserPropertyKey.PUSH_ENABLED
    override val value: Any get() = enabled
}

// com/kiero/core/analytic/property/AppVersion.kt
data class AppVersion(val versionName: String) : UserProperty {
    override val key: String get() = UserPropertyKey.APP_VERSION
    override val value: Any get() = versionName
}

enum class CreationMethod(val value: String) {
    MANUAL("manual"),
    AI("ai")
}

enum class DueDateType(val value: String) {
    TODAY("today"),
    TOMORROW("tomorrow"),
    FUTURE("future")
}

enum class DestinationScreen(val value: String) {
    PARENT_HOME("parent_home"),
    PARENT_NOTIFICATION_FEED("parent_notification_feed"),
    CHILD_JOURNEY("child_journey"),
    CHILD_MISSION("child_mission")
}

enum class Platform(override val value: String) : UserProperty {
    IOS("ios"),
    ANDROID("android");

    override val key: String get() = UserPropertyKey.PLATFORM
}