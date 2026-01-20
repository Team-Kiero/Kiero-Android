package com.kiero.core.localstorage.constant

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstant {
    val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    val KEY_USER_ROLE = stringPreferencesKey("user_role")
    val KEY_IS_SAW_ONBOARDING = booleanPreferencesKey("is_saw_onboarding")

    val KEY_PARENT_NAME = stringPreferencesKey("parent_name")
    val KEY_PARENT_PROFILE_IMAGE = stringPreferencesKey("parent_profile_image")
}
