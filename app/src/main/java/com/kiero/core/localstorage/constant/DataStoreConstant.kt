package com.kiero.core.localstorage.constant

import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConstant {
    val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}