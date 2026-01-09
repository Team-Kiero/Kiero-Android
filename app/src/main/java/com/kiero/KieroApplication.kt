package com.kiero

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KieroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDayMode()
        initKakaoSdk()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) {
            Timber.Forest.plant(Timber.DebugTree())
        }
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun initKakaoSdk() {
        try {
            KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
            // 태그를 명시적으로 지정
            Timber.tag("KAKAO_INIT").d("✅ 카카오 SDK 초기화 성공")
        } catch (e: Exception) {
            Timber.tag("KAKAO_INIT").e(e, "❌ 카카오 SDK 초기화 실패")
        }
    }
}