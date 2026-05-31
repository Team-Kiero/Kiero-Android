package com.kiero.core.localstorage.constant

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstant {
    // Token
    val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    // User Info
    val KEY_USER_ROLE = stringPreferencesKey("user_role")
    val KEY_IS_SAW_ONBOARDING = booleanPreferencesKey("is_saw_onboarding")
    val KEY_PARENT_NAME = stringPreferencesKey("parent_name")
    val KEY_PARENT_PROFILE_IMAGE = stringPreferencesKey("parent_profile_image")
    val KEY_CHILD_ID = stringPreferencesKey("child_id")
    val KEY_CHILD_LAST_NAME = stringPreferencesKey("key_child_last_name")
    val KEY_CHILD_FIRST_NAME = stringPreferencesKey("key_child_first_name")
    val KEY_PENDING_INVITE_CODE = stringPreferencesKey("key_pending_invite_code")
    val KEY_PENDING_INVITE_EXPIRE_TIME = longPreferencesKey("key_pending_invite_expire_time")

    // Permission
    const val KEY_PERMISSION_DENIED_COUNT = "permission_denied_count"

    // Terms
    val KEY_IS_REQUIRED_TERMS_ALL_AGREED = booleanPreferencesKey("is_required_terms_all_agreed")
    val KEY_AGREED_TERMS_IDS = stringPreferencesKey("agreed_terms_ids")

    // key 그룹핑
    val TOKEN_KEYS = listOf(
        KEY_ACCESS_TOKEN,
        KEY_REFRESH_TOKEN
    )

    val PARENT_INFO_KEYS = listOf(
        KEY_USER_ROLE,
        KEY_PARENT_NAME,
        KEY_PARENT_PROFILE_IMAGE,
        KEY_CHILD_ID,
        KEY_CHILD_LAST_NAME,
        KEY_CHILD_FIRST_NAME,
        KEY_PENDING_INVITE_CODE,
        KEY_PENDING_INVITE_EXPIRE_TIME,
        KEY_IS_REQUIRED_TERMS_ALL_AGREED,
        KEY_AGREED_TERMS_IDS,
    )

    val CHILD_INFO_KEYS = listOf(
        KEY_USER_ROLE,
        KEY_IS_SAW_ONBOARDING,
    )

}
