package com.kiero.core.designsystem.component.animation

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.drawable.MovieDrawable
import coil.request.ImageRequest
import coil.request.onAnimationEnd
import coil.request.repeatCount
import coil.size.Precision
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun KieroAnimationView(
    type: KieroAnimationType,
    modifier: Modifier = Modifier,
    repeatCount: Int = MovieDrawable.REPEAT_INFINITE,
    contentScale: ContentScale = ContentScale.Crop,
    onSuccess: () -> Unit = {}
) {
    val context = LocalContext.current

    when (type) {
        is KieroAnimationType.Lottie -> {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(type.resId))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = if (repeatCount == MovieDrawable.REPEAT_INFINITE)
                    LottieConstants.IterateForever else 1
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is KieroAnimationType.Image -> {
            val imageRequest = remember(type.resId) {
                ImageRequest.Builder(context)
                    .data(type.resId)
                    .precision(Precision.INEXACT)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .allowHardware(false)
                    .repeatCount(repeatCount)
                    .onAnimationEnd {
                        onSuccess()
                    }
                    .build()
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}