package com.kiero.data.config.constant

const val DEBUG_MIN_FETCH_INTERVAL_SECONDS = 5L * 60L
const val RELEASE_MIN_FETCH_INTERVAL_SECONDS = 60L * 60L // https://firebase.google.com/docs/remote-config/android/get-started 예제의 1시간 값

const val KEY_MIN_FORCE_VERSION = "min_force_version"
const val KEY_LATEST_VERSION = "latest_version"
