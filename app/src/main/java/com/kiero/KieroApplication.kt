package com.kiero

import android.app.Application
import android.graphics.Bitmap
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KieroApplication : Application(), ImageLoaderFactory {
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
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
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50L * 1024 * 1024)
                    .build()
            }
            .crossfade(false)
            .bitmapConfig(Bitmap.Config.HARDWARE)
            .respectCacheHeaders(false)
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}
