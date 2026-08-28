package com.kiero

import android.app.Application
import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.kakao.sdk.common.KakaoSdk
import com.kiero.core.network.di.NoAuthNetwork
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class KieroApplication : Application(), ImageLoaderFactory {
    @Inject
    @NoAuthNetwork
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        setTimber()
        setDayMode()
        initKakaoSdk()
        setCrashlytics()
        setAnalytics()
    }

    private fun setTimber() {
        if (BuildConfig.DEBUG) {
            Timber.Forest.plant(Timber.DebugTree())
        }
    }

    private fun setDayMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setCrashlytics() {
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        Firebase.crashlytics.setCustomKey("environment", BuildConfig.FLAVOR_environment)
    }

    private fun setAnalytics() {
        Firebase.analytics.setDefaultEventParameters(
            bundleOf("environment" to BuildConfig.FLAVOR_environment)
        )
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

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.15)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            .crossfade(false)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .allowHardware(true)
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}
