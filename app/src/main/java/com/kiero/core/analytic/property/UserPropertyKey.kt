package com.kiero.core.analytic.property


object UserPropertyKey {
    const val USER_ROLE = "user_role"
    const val LOGIN_METHOD = "login_method"
    const val NOTIFICATION_PERMISSION = "notification_permission"
    const val PUSH_ENABLED = "push_enabled"
    const val PLATFORM = "platform"
    const val APP_VERSION = "app_version"
    const val FAMILY_CONNECTION_ID = "family_connection_id"
}

enum class UserRole(val value: String) { PARENT("parent"), CHILD("child") }
enum class LoginMethod(val value: String) { KAKAO("kakao"), APPLE("apple"), INVITE_CODE("invite_code") }
enum class NotificationPermissionState(val value: String) {
    GRANTED("granted"), DENIED("denied"), NOT_DETERMINED("not_determined")
}
